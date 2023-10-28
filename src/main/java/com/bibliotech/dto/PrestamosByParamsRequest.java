package com.bibliotech.dto;

import com.bibliotech.entity.*;
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
    Long id;
    Instant fechaInicioEstimadaDesde;
    Instant fechaInicioEstimadaHasta;
    Instant fechaFinEstimadaDesde;
    Instant fechaFinEstimadaHasta;
    Instant fechaBajaDesde;
    Instant fechaBajaHasta;
    Long ejemplarId;
    List<Long> estadoPrestamoIds;
    List<PrestamoEstado> prestamosEstados;
    List<Long> multasIds;
    List<Long> usuariosIds;


    Boolean hasAnyFieldFilled() {
        return Objects.nonNull(id)
                || Objects.nonNull(fechaInicioEstimadaDesde)
                || Objects.nonNull(fechaInicioEstimadaHasta)
                || Objects.nonNull(fechaFinEstimadaDesde)
                || Objects.nonNull(fechaFinEstimadaHasta)
                || Objects.nonNull(fechaBajaDesde)
                || Objects.nonNull(fechaBajaHasta)
                || Objects.nonNull(ejemplarId)
                || Objects.nonNull(estadoPrestamoIds)
                || Objects.nonNull(prestamosEstados)
                || Objects.nonNull(multasIds)
                || Objects.nonNull(usuariosIds);
                
    }

    public void validate() {
        if (!hasAnyFieldFilled()) {
            throw new ValidationException("Algún parámetro debe venir completo");
        }
    }
}

