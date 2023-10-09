package com.bibliotech.security.dao.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record SignUpRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        @Min(5) @Max(30) String password,
        @NotNull List<Long> roleIds
){}