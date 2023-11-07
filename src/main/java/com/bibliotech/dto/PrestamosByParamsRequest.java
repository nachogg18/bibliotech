package com.bibliotech.dto;

import com.bibliotech.entity.EstadoPrestamo;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
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
    List<String> usuariosLegajos;
    List<String> usuariosEmails;
    List<String> usuariosDNIs;


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
                || Objects.nonNull(usuariosIds)
                || Objects.nonNull(usuariosLegajos)
                || Objects.nonNull(usuariosEmails)
                || Objects.nonNull(usuariosDNIs);
    }

    public void validate() {
        if (!hasAnyFieldFilled()) {
            throw new ValidationException("Algún parámetro debe venir completo");
        }
        
        if (!validateEstadoPrestamoNombres().isEmpty()) {
            throw new ValidationException(String.format("Los estado prestamo nombres son incorrectos: %s", validateEstadoPrestamoNombres()));
        }
    }
    
    private List<String> validateEstadoPrestamoNombres() {
        if (Objects.isNull(prestamosEstadosNombres)) {
            return List.of();
        }

        List<String> valoresIncorrectos = new ArrayList<>();
        prestamosEstadosNombres
                .stream()
                .forEach(
                        nombre -> {
                            if (Arrays.stream(EstadoPrestamo.values()).map(
                                EstadoPrestamo::name
                        ).filter(estadoPrestamoNombre -> estadoPrestamoNombre.equals(nombre)).findAny().isEmpty()) {
                                valoresIncorrectos.add(nombre);
                            }

                        }

                );
        
        return valoresIncorrectos;
    }
}

