package com.bibliotech.security.dao.request;

import com.bibliotech.security.entity.Role;
import com.bibliotech.utils.RoleUtils;
import com.bibliotech.validator.IsOptionalField;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;
import org.apache.commons.lang3.StringUtils;

public record UpdateRoleRequest(@NotNull Long id,
                                @IsOptionalField @Pattern(regexp = RoleUtils.VALID_ROLE_NAME_FORMAT) String name,
                                @NotNull Boolean enabled) {

    public Role toRole(Role existingRole) {
        
        if (StringUtils.isNotBlank(this.name)) {
            existingRole.setName(this.name);
        }

        if (this.enabled) {
            existingRole.setEndDate(null);
        } else {
            existingRole.setEndDate(Instant.now());
        }

        return existingRole;

    }


}
