package com.bibliotech.security.dao.request;

import jakarta.validation.constraints.*;

public record SignUpRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        @Min(5) @Max(30) String password
){}