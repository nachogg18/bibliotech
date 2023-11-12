package com.bibliotech.security.dao.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserInfoRequest(
        @NotNull @Pattern(regexp = "\\d{8}") String dni,
        @NotNull String legajo,
        @NotNull String direccionContacto,
        String emailContacto,
        @NotNull Long localidadId,
        @NotBlank String telefonoContacto
) {
}



