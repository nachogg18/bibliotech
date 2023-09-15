package com.bibliotech.security.dao.request;

import com.bibliotech.utils.RoleUtils;
import jakarta.validation.constraints.Pattern;

public record CreateRoleRequest(@Pattern(regexp = RoleUtils.VALID_ROL_NAME_FORMAT) String name) {}

