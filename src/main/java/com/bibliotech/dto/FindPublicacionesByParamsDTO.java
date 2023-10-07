package com.bibliotech.dto;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;

public record FindPublicacionesByParamsDTO(String titulo, String autor, String isbn, String anio) {

  // TODO mejorar validacion de request
  Boolean hasAnyFieldFilled() {
    return StringUtils.isNotBlank(titulo)
        || StringUtils.isNotBlank(autor)
        || StringUtils.isNotBlank(isbn)
        || StringUtils.isNotBlank(anio);
  }

  public void validate() {
    if (!hasAnyFieldFilled()) {
      throw new ValidationException("Algún parámetro debe venir completo");
    }
  }
}
