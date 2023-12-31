package com.bibliotech.security.dao.request;

import jakarta.validation.constraints.*;

public record SignUpRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        String password
){}