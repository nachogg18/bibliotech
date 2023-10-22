package com.bibliotech.security.dao.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record SignUpWithoutRequiredConfirmationRequest(
        @NotNull @Valid SignUpRequest signUpRequest,
        @NotNull List<Long> roleIds
){}