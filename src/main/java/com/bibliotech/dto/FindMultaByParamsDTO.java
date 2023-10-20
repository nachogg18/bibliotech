package com.bibliotech.dto;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

public record FindMultaByParamsDTO(String idUsuario, String idPrestamo, Instant fechaDesde, Instant fechaHasta) {

    // TODO mejorar validacion de request
    Boolean hasAnyFieldFilled() {
        return StringUtils.isNotBlank(idUsuario)
                || StringUtils.isNotBlank(idPrestamo)
                || fechaDesde != null
                || fechaHasta != null
                ;
    }

    public void validate() {
        if (!hasAnyFieldFilled()) {
            throw new ValidationException("Algún parámetro debe venir completo");
        }
    }
}
