package com.bibliotech.security.dao.request;

import com.bibliotech.utils.RoleUtils;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public record CreateRoleRequest(
        @Pattern(regexp = RoleUtils.VALID_ROLE_NAME_FORMAT) String name,
        Set<Long> privilegesIdsToAssign
) {}

