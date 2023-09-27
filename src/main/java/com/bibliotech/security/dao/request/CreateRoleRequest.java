package com.bibliotech.security.dao.request;

import com.bibliotech.utils.RoleUtils;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record CreateRoleRequest(
        @Pattern(regexp = RoleUtils.VALID_ROL_NAME_FORMAT) String name,
        List<Long> privilegesIdsToAssign
) {}

