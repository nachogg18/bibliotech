package com.bibliotech.security.dao.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record GetPrivilegesFromRoleRequest(@JsonProperty("role_ids") List<String> roleIds) {


    private static final Logger logger = LoggerFactory.getLogger(GetPrivilegesFromRoleRequest.class); 


    public List<Long> validateRequest() {
        return roleIds.stream().map(
                roleId -> {
                    long longId = Long.parseLong(roleId);
                    logger.info(String.format("long id parsed for %s: %s", roleId, longId));
                    return longId;
                }
        ).collect(Collectors.toList());
    }

}

