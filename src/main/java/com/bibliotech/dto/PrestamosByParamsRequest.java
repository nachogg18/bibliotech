package com.bibliotech.dto;

import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class PrestamosByParamsRequest {
    List<Long> prestamosIds;
    Instant fechaInicioEstimadaDesde;
    Instant fechaInicioEstimadaHasta;
    Instant fechaFinEstimadaDesde;
    Instant fechaFinEstimadaHasta;
    Instant fechaAltaDesde;
    Instant fechaAltaHasta;
    Instant fechaBajaDesde;
    Instant fechaBajaHasta;
    List<Long> ejemplaresIds;
    List<Long> prestamosEstadosIds;
    List<String> prestamosEstadosNombres;
    List<Long> multasIds;
    List<Long> usuariosIds;


    Boolean hasAnyFieldFilled() {
        return Objects.nonNull(prestamosIds)
                || Objects.nonNull(fechaInicioEstimadaDesde)
                || Objects.nonNull(fechaInicioEstimadaHasta)
                || Objects.nonNull(fechaFinEstimadaDesde)
                || Objects.nonNull(fechaFinEstimadaHasta)
                || Objects.nonNull(fechaAltaDesde)
                || Objects.nonNull(fechaAltaHasta)
                || Objects.nonNull(fechaBajaDesde)
                || Objects.nonNull(fechaBajaHasta)
                || Objects.nonNull(ejemplaresIds)
                || Objects.nonNull(prestamosEstadosIds)
                || Objects.nonNull(prestamosEstadosNombres)
                || Objects.nonNull(multasIds)
                || Objects.nonNull(usuariosIds);
                
    }

    public void validate() {
        if (!hasAnyFieldFilled()) {
            throw new ValidationException("Algún parámetro debe venir completo");
        }
    }
}

