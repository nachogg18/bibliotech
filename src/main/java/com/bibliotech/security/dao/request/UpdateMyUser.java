package com.bibliotech.security.dao.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateMyUser {

    @NotBlank String firstName;
    @NotBlank String lastName;
    @NotBlank @Email String email;
    @NotNull @Pattern(regexp = "\\d{8}") String dni;
    @NotNull String legajo;
    @NotNull String direccionContacto;
    String emailContacto;
    @NotNull Long localidadId;
    @NotBlank String telefonoContacto;
    @NotNull Long carreraId;
    @NotNull Long facultadId;
}
