package com.bibliotech.security.dao.request;

import com.bibliotech.security.entity.Role;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public record CreateRoleResponse(
        String id,
        String name,
        String creationDate,
        String endDate
) {

    public static CreateRoleResponse fromRole(Role role) {
        return new CreateRoleResponse(
                (!Objects.isNull(role.getId())) ? role.getId().toString(): StringUtils.EMPTY,
                (!Objects.isNull(role.getName())) ? role.getName() : StringUtils.EMPTY,
                (!Objects.isNull(role.getStartDate())) ? role.getStartDate().toString() : StringUtils.EMPTY,
                (!Objects.isNull(role.getEndDate())) ? role.getEndDate().toString() : StringUtils.EMPTY
        );
    }
}
