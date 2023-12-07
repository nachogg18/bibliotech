package com.bibliotech.service;

import com.bibliotech.dto.CreateMultaDTO;
import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;
import com.bibliotech.dto.MultaResponse;
import com.bibliotech.entity.*;
import com.bibliotech.repository.MultaRepository;
import com.bibliotech.repository.specifications.MultaSpecifications;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MultaServiceImpl implements MultaService {
    private final MultaRepository multaRepository;
    private final UserService userService;
    private final PrestamoService prestamoService;
    private final TipoMultaService tipoMultaService;
    private final NotificacionService notificacionService;

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
    public boolean createMultaPrestamo(CreateMultaDTO request) throws Exception {
        User usuarioMultado = userService.findById(request.getIdUsuario())
                .orElseThrow(() -> new ValidationException(String.format("no existe User con id: %s", request.getIdUsuario())));
        Prestamo prestamoMultado = prestamoService.findById(request.getIdPrestamo());

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
                .build();
        try {
            multaRepository.save(multa);
        } catch (Exception e) {
            return false;
        }

        notificacionService.crearNotificacion(
                usuarioMultado,
                String.format("Fue multado por el préstamo con la publicación %s",
                        prestamoMultado.getEjemplar().getPublicacion().getTitulo()), TipoNotificacion.MULTA_CREADA
        );

        return true;
    }

    @Override
    public MultaResponse finalizarMulta(Long id) {
        Multa multa = multaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe multa con id %s", id)));
        multa.getMultaEstados().stream().filter(
                multaEstado -> multaEstado.getFechaFin() == null
        ).findFirst().orElseThrow(() -> new RuntimeException("Error con la multa")).setFechaFin(Instant.now());
        multa.getMultaEstados().add(
                MultaEstado.builder()
                        .estadoMulta(EstadoMulta.FINALIZADA)
                        .fechaInicio(Instant.now())
                        .build()
        );
        multaRepository.save(multa);

        notificacionService.crearNotificacion(
                multa.getUser(),
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
        MultaEstado estado = multa.getMultaEstados().stream().filter(
                multaEstado -> multaEstado.getFechaFin() == null
        ).findFirst().orElseThrow(() -> new RuntimeException("Error con la multa"));
        if (estado.getEstadoMulta() != EstadoMulta.ACTIVA) {
            estado.setFechaFin(Instant.now());
            multa.getMultaEstados().add(
                    MultaEstado.builder()
                            .estadoMulta(EstadoMulta.FINALIZADA)
                            .fechaInicio(Instant.now())
                            .build()
            );
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
        List<Multa> multas = multaRepository.findAllByFechaFinBefore(Instant.now());
        for (Multa multa : multas) {
            multa.setFechaBaja(Instant.now());
            MultaEstado estadoActual = multa.getMultaEstados().stream().filter(
                    multaEstado -> multaEstado.getFechaFin() == null
            )
                    .findFirst().
                    orElseThrow(() -> new RuntimeException("Error con la multa"));
            estadoActual.setFechaFin(Instant.now());
            multa.getMultaEstados().add(
                    MultaEstado.builder()
                            .estadoMulta(EstadoMulta.FINALIZADA)
                            .fechaInicio(Instant.now())
                            .build()
            );
            multaRepository.save(multa);
            notificacionService.crearNotificacion(
                    multa.getUser(),
                    String.format("Multa sobre préstamo con publicación %s finalizada", multa.getPrestamo().getEjemplar().getPublicacion().getTitulo()),
                    TipoNotificacion.MULTA_FINALIZADA
            );
        }
    }

    @Scheduled(cron = "0 0 0 * * *") // medianoche todos los dias
    public void crearMultaAutomatico() {
        List<Prestamo> prestamos = prestamoService.findAllByFechaBajaNullAndFechaFinBefore(Instant.now());
        for (Prestamo prestamo : prestamos) {
            prestamo.setFechaBaja(Instant.now());
            PrestamoEstado estadoActual = prestamo.getEstado().stream().filter(
                            multaEstado -> multaEstado.getFechaFin() == null
                    )
                    .findFirst().
                    orElseThrow(() -> new RuntimeException("Error con la multa"));
            estadoActual.setFechaFin(Instant.now());
            prestamo.getEstado().add(
                    PrestamoEstado.builder()
                            .fechaInicio(Instant.now())
                            .estado(EstadoPrestamo.VENCIDO)
                            .build()
            );
            try {
                prestamoService.save(prestamo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            notificacionService.crearNotificacion(
                    prestamo.getUsuario(),
                    String.format("Préstamo con publicación %s vencido, multa aplicada", prestamo.getEjemplar().getPublicacion().getTitulo()),
                    TipoNotificacion.PRESTAMO_VENCIDO
            );
        }
    }
}
