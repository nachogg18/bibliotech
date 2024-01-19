package com.bibliotech.service;

import com.bibliotech.dto.CreateMultaDTO;
import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;
import com.bibliotech.dto.MultaResponse;
import com.bibliotech.entity.*;
import com.bibliotech.repository.MultaEstadoRepository;
import com.bibliotech.repository.MultaRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.repository.specifications.MultaSpecifications;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.UserService;
import com.bibliotech.security.service.impl.UserServiceImpl;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

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
                                .estado(multa.getMultaEstados().isEmpty() ? null : multa.getMultaEstados().stream().filter(me -> me.getFechaFin() == null).toList().get(0).getNombre())
                                .tipo(multa.getTipoMulta() == null ? null : multa.getTipoMulta().getNombre())
                                .build()
                ).toList();
    }

    @Override
    public boolean createMulta(CreateMultaDTO request) throws Exception {
        User usuarioMultado = userService.findById(request.getIdUsuario())
                .orElseThrow(() -> new ValidationException(String.format("no existe User con id: %s", request.getIdUsuario())));
        Prestamo prestamoMultado = prestamosRepository.findById(request.getIdPrestamo()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No se encontró prestamo con id %s", request.getIdPrestamo())));
        Multa multa = Multa.builder()
                .prestamo(prestamoMultado)
                .user(usuarioMultado)
                .tipoMulta(
                        tipoMultaService.findById(request.getIdMotivoMulta())
                                .orElseThrow(() -> new ValidationException(String.format("no existe TipoMulta con id: %s", request.getIdMotivoMulta())))
                )
                .multaEstados(
                        List.of(
                                MultaEstado.builder()
                                        .fechaInicio(Instant.now())
                                        .estadoMulta(EstadoMulta.ACTIVA)
                                        .build())
                )
                .fechaInicio(Instant.now())
                .fechaAlta(Instant.now())
                .fechaFin(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
        try {
            multaRepository.save(multa);
        } catch (Exception e) {
            return false;
        }

        notificacionService.crearNotificacion(
                usuarioMultado.getId(),
                String.format("Fue multado por el préstamo con la publicación %s",
                        prestamoMultado.getEjemplar().getPublicacion().getTitulo()),
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
                                .fechaDesde(multa.getFechaInicio())
                                .fechaHasta(multa.getFechaFin())
                                .estado(Objects.requireNonNull(multa.getMultaEstados().stream()
                                                        .filter(estado -> estado.getFechaFin() == null)
                                                        .findFirst()
                                                        .orElse(null))
                                                .getNombre()
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

    @Scheduled(cron = "0 0 0 * * *") // medianoche todos los dias
    public void finalizarMultaAutomatico() {
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
    }

    @Scheduled(cron = "0 0 0 * * *") // medianoche todos los dias
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
                        .idMotivoMulta(1L)
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
