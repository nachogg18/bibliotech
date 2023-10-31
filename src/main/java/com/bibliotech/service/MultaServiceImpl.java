package com.bibliotech.service;

import com.bibliotech.dto.CreateMultaDTO;
import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;
import com.bibliotech.entity.EstadoMulta;
import com.bibliotech.entity.Multa;
import com.bibliotech.entity.MultaEstado;
import com.bibliotech.repository.MultaRepository;
import com.bibliotech.repository.specifications.MultaSpecifications;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.UserService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MultaServiceImpl implements MultaService {
    private final MultaRepository multaRepository;
    private final UserService userService;
    private final PrestamoService prestamoService;
    private final TipoMultaService tipoMultaService;

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
                                .estado(multa.getMultaEstados().size() == 0 ? null : multa.getMultaEstados().stream().filter(me -> me.getFechaFin() == null).toList().get(0).getNombre())
                                .tipo(multa.getTipoMulta() == null ? null : multa.getTipoMulta().getNombre())
                                .build()
                ).toList();
    }

    @Override
    public boolean createMulta(CreateMultaDTO request) throws Exception {
        Multa multa = Multa.builder()
                .prestamo(
                        prestamoService.findById(request.getIdPrestamo())
                )
                .user(
                        userService.findById(request.getIdUsuario())
                                .orElseThrow(() -> new ValidationException(String.format("no existe User con id: %s", request.getIdUsuario())))
                )
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
        return true;
    }

    @Override
    public List<MultaItemTablaDTO> getMultasByUserId(Long idUsuario) {
        return multaRepository.findMultasByUsuarioId(idUsuario)
                .stream().map(
                        multa -> MultaItemTablaDTO
                                .builder()
                                .id(multa.getId())
                                .idPrestamo(multa.getPrestamo().getId())
                                .idUsuario(multa.getUser().getId())
                                .fechaDesde(multa.getFechaInicio())
                                .fechaHasta(multa.getFechaFin())
                                .estado(multa.getMultaEstados().stream()
                                        .filter(estado -> estado.getFechaFin() == null)
                                        .findFirst()
                                        .orElse(null)
                                                .getNombre()
                                        )
                                .tipo(multa.getTipoMulta().getNombre())
                                .build()
                ).toList();
    }

    @Override
    public boolean isUsuarioHabilitado(Long id) {
        User usuario = userService.findById(id).get();
        List<MultaItemTablaDTO> multasUsuario = getMultasByUserId(usuario.getId());
        if (multasUsuario.stream().filter(
                multa -> multa.getEstado() == "ACTIVA"
        ).findAny().orElse(null) == null) return true;
        return false;
    }
}
