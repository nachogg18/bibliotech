package com.bibliotech.security.dao.request;

import com.bibliotech.utils.PrivilegeUtils;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public record CreatePrivilegeRequest(
        @Pattern(regexp = PrivilegeUtils.VALID_PRIVILEGE_NAME_FORMAT) String name,
        @NotNull Long resourceId,
        @NotNull List<String> actions
) {}

