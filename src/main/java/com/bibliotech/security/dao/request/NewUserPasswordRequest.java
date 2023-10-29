package com.bibliotech.security.dao.request;


import jakarta.validation.constraints.NotNull;

public record NewUserPasswordRequest(
        VerificationUserRequest verificationUserRequest,
        @NotNull String newPassword
) {
}
