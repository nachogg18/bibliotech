package com.bibliotech.security.dao.request;


import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AssignRoleRequest(
        @NotNull
        Long userId,
        @NotNull
        List<Long> roleIds
) {}

