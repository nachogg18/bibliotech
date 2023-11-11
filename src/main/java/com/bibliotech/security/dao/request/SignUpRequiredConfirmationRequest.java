package com.bibliotech.security.dao.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record SignUpRequiredConfirmationRequest(
        @NotNull @Valid SignUpRequest signUpRequest,
        @Valid UserInfoRequest userInfo
){}