package com.bibliotech.security.dao.request;

import jakarta.validation.constraints.*;

public record VerificationUserRequest(
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "/w{6}") String code
){}