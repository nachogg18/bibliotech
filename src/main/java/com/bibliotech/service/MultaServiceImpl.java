package com.bibliotech.service;

import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;
import com.bibliotech.entity.Multa;
import com.bibliotech.repository.MultaRepository;
import com.bibliotech.repository.MultaSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MultaServiceImpl implements MultaService {
    private final MultaRepository multaRepository;

    public MultaServiceImpl(MultaRepository multaRepository) {
        this.multaRepository = multaRepository;
    }

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
}
