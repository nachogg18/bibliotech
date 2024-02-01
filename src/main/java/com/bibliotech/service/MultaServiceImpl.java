package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.repository.MultaEstadoRepository;
import com.bibliotech.repository.MultaRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.repository.specifications.MultaSpecifications;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultaServiceImpl implements MultaService {
    private final MultaRepository multaRepository;
    private final UserService userService;
    private final PrestamosRepository prestamosRepository;
    private final NotificacionService notificacionService;
    private final TipoMultaService tipoMultaService;
    private final PrestamoEstadoRepository prestamoEstadoRepository;
    private final MultaEstadoRepository multaEstadoRepository;
    private final AuthenticationService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(MultaServiceImpl.class);

    @Override
    public List<MultaItemTablaDTO> findByParams(FindMultaByParamsDTO request) {
        Specification<Multa> spec = MultaSpecifications.empty();

        String userId = request.idUsuario();
        if (StringUtils.isNotBlank(userId)) {
            spec = spec.and(MultaSpecifications.hasUserId(userId));
        }

        String prestamoId = request.idPrestamo();
        if (StringUtils.isNotBlank(prestamoId)) {
            spec = spec.and(MultaSpecifications.hasPrestamoId(prestamoId));
        }

        Instant fechaDesde = request.fechaDesde();
        if (fechaDesde != null) {
            spec = spec.and(MultaSpecifications.hasFechaDesdeLessThanOrEqualTo(fechaDesde));
        }

        Instant fechaHasta = request.fechaHasta();
        if (fechaHasta != null) {
            spec = spec.and(MultaSpecifications.hasFechaHastaGreaterThanOrEqualTo(fechaHasta));
        }

        return multaRepository.findAll(spec)
                .stream().map(
                        multa -> MultaItemTablaDTO.builder()
                                .id(multa.getId())
                                .idPrestamo(multa.getPrestamo() == null ? null : multa.getPrestamo().getId())
                                .idUsuario(multa.getUser().getId())
                                .fechaDesde(multa.getFechaInicio())
                                .fechaHasta(multa.getFechaFin())
                                .estado(multa.getMultaEstados().size() == 0 ? null : multa.getMultaEstados().stream().filter(me -> me.getFechaFin() == null).toList().get(0).getEstadoMulta().name())
                                .tipo(multa.getTipoMulta() == null ? null : multa.getTipoMulta().getNombre())
                                .build()
                ).toList();
    }

    @Override
    public boolean createMulta(CreateMultaDTO request) throws Exception {

        TipoMulta tipoMulta = tipoMultaService.findById(request.getIdMotivoMulta()).orElseThrow(() -> new ValidationException(String.format("No existe TipoMulta con id: %s", request.getIdMotivoMulta())));
        User user = userService.findById(request.getIdUsuario()).orElseThrow(() -> new ValidationException(String.format("No existe User con id: %s", request.getIdUsuario())));
        Prestamo prestamo = prestamosRepository.findById(request.getIdPrestamo()).orElseThrow(() -> new ValidationException(String.format("No existe prestamo con id: %s", request.getIdPrestamo())));

        Multa multa = Multa.builder()
                .prestamo(prestamo)
                .user(user)
                .tipoMulta(tipoMulta)
                .descripcion(request.getDescripcion())
                .multaEstados(
                        request.getFechaInicioMulta().compareTo(Instant.now().truncatedTo(java.time.temporal.ChronoUnit.DAYS)) == 0
                        ? List.of(MultaEstado.builder().fechaInicio(Instant.now()).estadoMulta(EstadoMulta.ACTIVA ).build())
                        : List.of(MultaEstado.builder().fechaInicio(Instant.now()).estadoMulta(EstadoMulta.PENDIENTE ).build())
                )
                .fechaAlta(Instant.now())
                .fechaInicio(request.getFechaInicioMulta().truncatedTo(ChronoUnit.DAYS))
                .fechaFin(request.getFechaInicioMulta().truncatedTo(ChronoUnit.DAYS).plus(Duration.ofDays(tipoMulta.getCantidadDias())))
                .build();
        try {
            multaRepository.save(multa);
        } catch (Exception e) {
            return false;
        }

        notificacionService.crearNotificacion(
                user.getId(),
                String.format("Fue multado por el préstamo con la publicación %s", prestamo.getEjemplar().getPublicacion().getTitulo()),
                TipoNotificacion.MULTA_CREADA
        );

        return true;
    }

    @Override
    public List<MultaItemTablaDTO> getMultasByUserId(Long idUsuario) {
        return multaRepository.findByUserId(idUsuario)
                .stream().map(
                        multa -> MultaItemTablaDTO
                                .builder()
                                .id(multa.getId())
                                .idPrestamo(multa.getPrestamo().getId())
                                .idUsuario(multa.getUser().getId())
                                .fechaDesde(multa.getFechaInicio().truncatedTo(java.time.temporal.ChronoUnit.DAYS))
                                .fechaHasta(multa.getFechaFin().truncatedTo(java.time.temporal.ChronoUnit.DAYS))
                                .estado(
                                        multa.getMultaEstados().stream()
                                        .filter(estado -> estado.getFechaFin() == null)
                                        .findFirst().orElse(null).getEstadoMulta().name()
                                )
                                .tipo(multa.getTipoMulta().getNombre())
                                .build()
                ).toList();
    }

    @Override
    public boolean isUsuarioHabilitado(Long id) {
        User usuario = userService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Usuario con id %s inhabilitado", id)));
        List<MultaItemTablaDTO> multasUsuario = getMultasByUserId(usuario.getId());
        return multasUsuario.stream().filter(
                multa -> Objects.equals(multa.getEstado(), "ACTIVA")
        ).findAny().orElse(null) == null;
    }

    @Override
    @Transactional
    public boolean deleteMulta(Long id) {
        Optional<Multa> multa = multaRepository.findById(id);
        if(!multa.isPresent()){ throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Multa no encontrada con el id."); }
        Optional<MultaEstado> estadoMulta = multa.get().getMultaEstados().stream().filter(multaEstado -> Objects.isNull(multaEstado.getFechaFin())).findFirst();
        if(estadoMulta.get().getEstadoMulta() != EstadoMulta.PENDIENTE && estadoMulta.get().getEstadoMulta() != EstadoMulta.ACTIVA){ throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "La multa ya ha finalizado."); }
        multa.get().setFechaBaja(Instant.now());
        List<MultaEstado> historialEstado = multa.get().getMultaEstados();
        MultaEstado multaEstado = historialEstado.stream().filter(multaEstado1 -> Objects.isNull(multaEstado1.getFechaFin())).findFirst().get();
        MultaResponse response = multaEstado.getEstadoMulta() == EstadoMulta.PENDIENTE ? this.cancelarMulta(id) : this.finalizarMulta(id);
        return true;
//        historialEstado.forEach(multaEstado -> {
//            if(Objects.isNull(multaEstado.getFechaFin())){
//                multaEstado.setFechaFin(Instant.now());
//            }
//        });
//        historialEstado.add(
//                MultaEstado
//                        .builder()
//                        .estadoMulta(EstadoMulta.CANCELADA)
//                        .fechaInicio(Instant.now())
//                        .build()
//        );
//        multa.get().setMultaEstados(historialEstado);
//        try {
//            multaRepository.save(multa.get());
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
    };

    @Override
    @Transactional
    public MultaDetalleDTO getMultaDetalle(Long id){
        Optional<Multa> multa = multaRepository.findById(id);
        if(!multa.isPresent()){ throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Multa no encontrada con el id."); }
        MultaDetalleDTO multaDetalleDTO = MultaDetalleDTO
                .builder()
                .multaEstados(multa.get().getMultaEstados())
                .tipoMulta(multa.get().getTipoMulta())
                .descripcion(multa.get().getDescripcion())
                .fechaInicio(multa.get().getFechaInicio())
                .fechaFin(multa.get().getFechaFin())
                .fechaAlta(multa.get().getFechaAlta())
                .fechaBaja(multa.get().getFechaBaja())
                .idUser(multa.get().getUser().getId())
                .dniUser(multa.get().getUser().getUserInfo().getDni())
                .nombreUser(multa.get().getUser().getFirstName() + ' ' + multa.get().getUser().getLastName())
                .legajoUser(multa.get().getUser().getUserInfo().getLegajo())
                .idPrestamo(multa.get().getPrestamo().getId())
                .estadoPrestamo(multa.get().getPrestamo().getEstado().get(multa.get().getPrestamo().getEstado().size() - 1).getEstado().name())
                .tituloPublicacionPrestamo(multa.get().getPrestamo().getEjemplar().getPublicacion().getTitulo())
                .idEjemplarPublicacionPrestamo(multa.get().getPrestamo().getEjemplar().getId())
                .fechaDesdePrestamo(multa.get().getPrestamo().getFechaInicioEstimada())
                .fechaHastaPrestamo(multa.get().getPrestamo().getFechaFinEstimada())
                .build();
        return multaDetalleDTO;
    };

    @Override
    @Transactional
    public boolean updateMulta(CreateMultaDTO request, Long id) {

        Optional<Multa> multa = multaRepository.findById(id);
        if (!multa.isPresent()){ throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Multa con id con econtrada"); }
        Optional<MultaEstado> estadoMulta = multa.get().getMultaEstados().stream().filter(multaEstado -> Objects.isNull(multaEstado.getFechaFin())).findFirst();
        if(!estadoMulta.isPresent() || estadoMulta.get().getEstadoMulta() != EstadoMulta.PENDIENTE){ throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "La multa debe estar con estado PENDIENTE"); }

        Optional<Prestamo> prestamo = prestamosRepository.findById(request.getIdPrestamo());
        if (!multa.isPresent()){ throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Prestamo con id con econtrado"); }

        Optional<User> usuario = userService.findById(request.getIdUsuario());
        if (!usuario.isPresent()){ throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Usuario con id con econtrado"); }

        Optional<TipoMulta> tipo = tipoMultaService.findById(request.getIdMotivoMulta());
        if (!tipo.isPresent()){ throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Tipo de multa con id con econtrada"); }

        multa.get().setTipoMulta(tipo.get());
        multa.get().setUser(usuario.get());
        multa.get().setPrestamo(prestamo.get());
        multa.get().setFechaInicio(request.getFechaInicioMulta().truncatedTo(ChronoUnit.DAYS));
        multa.get().setFechaFin(multa.get().getFechaInicio().truncatedTo(ChronoUnit.DAYS).plus(Duration.ofDays(tipo.get().getCantidadDias())));
        multa.get().setDescripcion(request.getDescripcion());

        try {
            multaRepository.save(multa.get());
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    @Override
    public MultaResponse finalizarMulta(Long id) {
        Multa multa = multaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe multa con id %s", id)));
        MultaEstado estadoActual = multa.getMultaEstados().stream().filter(
                multaEstado -> multaEstado.getFechaFin() == null
        ).findFirst().orElseThrow(() -> new RuntimeException("Error con la multa"));
        estadoActual.setFechaFin(Instant.now());
        MultaEstado nuevoEstado = new MultaEstado();
        nuevoEstado.setEstadoMulta(EstadoMulta.FINALIZADA);
        nuevoEstado.setFechaInicio(Instant.now());
        multaEstadoRepository.save(nuevoEstado);
        multa.getMultaEstados().add(nuevoEstado);
        multaRepository.save(multa);

        notificacionService.crearNotificacion(
                multa.getUser().getId(),
                String.format("Multa sobre préstamo con publicación %s finalizada", multa.getPrestamo().getEjemplar().getPublicacion().getTitulo()),
                TipoNotificacion.MULTA_FINALIZADA
        );

        return MultaResponse.builder()
                .multaId(multa.getId())
                .prestamoId(multa.getPrestamo().getId())
                .publicacionTitulo(multa.getPrestamo().getEjemplar().getPublicacion().getTitulo())
                .fechaInicioMulta(multa.getFechaInicio())
                .fechaFinMulta(multa.getFechaFin())
                .multaDescripcion(multa.getDescripcion())
                .estadoMulta(Objects.requireNonNull(multa.getMultaEstados().stream().filter(
                        multaEstado -> multaEstado.getFechaFin() == null
                ).findFirst().orElse(null)).getEstadoMulta().name())
                .build();
    }

    @Override
    public MultaResponse cancelarMulta(Long id) {
        Multa multa = multaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe multa con id %s", id)));
        MultaEstado estadoActual = multa.getMultaEstados().stream().filter(
                multaEstado -> multaEstado.getFechaFin() == null
        ).findFirst().orElseThrow(() -> new RuntimeException("Error con la multa"));
        if (estadoActual.getEstadoMulta() != EstadoMulta.ACTIVA) {
            estadoActual.setFechaFin(Instant.now());
            MultaEstado nuevoEstado = new MultaEstado();
            nuevoEstado.setEstadoMulta(EstadoMulta.CANCELADA);
            nuevoEstado.setFechaInicio(Instant.now());
            multaEstadoRepository.save(nuevoEstado);
            multa.getMultaEstados().add(nuevoEstado);
            multaRepository.save(multa);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Multa no se encuentra activa");
        }

        return MultaResponse.builder()
                .multaId(multa.getId())
                .prestamoId(multa.getPrestamo().getId())
                .publicacionTitulo(multa.getPrestamo().getEjemplar().getPublicacion().getTitulo())
                .fechaInicioMulta(multa.getFechaInicio())
                .fechaFinMulta(multa.getFechaFin())
                .multaDescripcion(multa.getDescripcion())
                .estadoMulta(Objects.requireNonNull(multa.getMultaEstados().stream().filter(
                        multaEstado -> multaEstado.getFechaFin() == null
                ).findFirst().orElse(null)).getEstadoMulta().name())
                .build();
    }

    @Override
    public List<MultaResponse> getMultaOfActiveUser() {
        User usuario = authenticationService.getActiveUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error con el usuario loggeado"));
        return multaRepository.findByUserId(usuario.getId()).stream().map(
                multa -> MultaResponse.builder()
                        .multaId(multa.getId())
                        .prestamoId(multa.getPrestamo().getId())
                        .publicacionTitulo(multa.getPrestamo().getEjemplar().getPublicacion().getTitulo())
                        .fechaInicioMulta(multa.getFechaInicio())
                        .fechaFinMulta(multa.getFechaFin())
                        .multaDescripcion(multa.getDescripcion())
                        .estadoMulta(Objects.requireNonNull(multa.getMultaEstados().stream().filter(
                                multaEstado -> multaEstado.getFechaFin() == null
                        ).findFirst().orElse(null)).getEstadoMulta().name())
                        .tipoMulta(multa.getTipoMulta().getNombre())
                        .build()
        ).toList();
    }

    @Scheduled(cron = "0 0 3 * * *") // 3am todos los dias
    public void finalizarMultaAutomatico() {
        logger.info("Iniciando proceso de finalización de multas automático.");
        List<Multa> multas = multaRepository.findAllByFechaBajaNullAndFechaFinBefore(Instant.now());
        for (Multa multa : multas) {
            multa.setFechaBaja(Instant.now());
            MultaEstado estadoActual = multa.getMultaEstados().stream().filter(
                            multaEstado -> multaEstado.getFechaFin() == null
                    )
                    .findFirst().
                    orElseThrow(() -> new RuntimeException("Error con la multa"));
            estadoActual.setFechaFin(Instant.now());
            MultaEstado nuevoEstado = new MultaEstado();
            nuevoEstado.setEstadoMulta(EstadoMulta.FINALIZADA);
            nuevoEstado.setFechaInicio(Instant.now());
            multaEstadoRepository.save(nuevoEstado);
            multa.getMultaEstados().add(nuevoEstado);

            multaRepository.save(multa);
            notificacionService.crearNotificacion(
                    multa.getUser().getId(),
                    String.format("Multa sobre préstamo con publicación %s finalizada", multa.getPrestamo().getEjemplar().getPublicacion().getTitulo()),
                    TipoNotificacion.MULTA_FINALIZADA
            );
        }
        logger.info("Proceso de finalización de multas automático finalizado. {} multas finalizadas.",multas.size());
    }

    @Scheduled(cron = "0 0 3 * * *") // 3am todos los dias
    public void crearMultaAutomatico() {
        logger.info("Iniciando proceso de creación de multas automático.");
        List<Prestamo> prestamos = prestamosRepository.findAllByFechaBajaNullAndFechaFinEstimadaBefore(Instant.now());
        for (Prestamo prestamo : prestamos) {
            prestamo.setFechaBaja(Instant.now());

            //cambio de estados
            PrestamoEstado estadoActual = prestamo.getEstado().stream()
                    .filter(estado -> estado.getFechaFin() == null)
                    .findFirst()
                    .orElse(null);
            if (estadoActual == null) throw new ValidationException("Error con el préstamo");

            estadoActual.setFechaFin(Instant.now());
            PrestamoEstado prestamoEstado = new PrestamoEstado();
            prestamoEstado.setEstado(EstadoPrestamo.VENCIDO);
            prestamoEstadoRepository.save(prestamoEstado);
            prestamo.getEstado().add(prestamoEstado);

            try {
                createMulta(CreateMultaDTO.builder()
                        .idMotivoMulta(tipoMultaService.findByNombre("Multa por retraso").getId())
                        .fechaInicioMulta(Instant.now())
                        .idUsuario(prestamo.getUsuario().getId())
                        .idPrestamo(prestamo.getId())
                        .build());
                prestamosRepository.save(prestamo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            notificacionService.crearNotificacion(
                    prestamo.getUsuario().getId(),
                    String.format("Préstamo con publicación %s vencido, multa aplicada", prestamo.getEjemplar().getPublicacion().getTitulo()),
                    TipoNotificacion.PRESTAMO_VENCIDO
            );


        }
        logger.info("Proceso de creación de multas automático finalizado. {} multas creadas.",prestamos.size());
    }


}
