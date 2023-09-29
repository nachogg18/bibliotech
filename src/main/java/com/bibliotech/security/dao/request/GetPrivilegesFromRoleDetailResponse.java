package com.bibliotech.security.dao.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GetPrivilegesFromRoleDetailResponse(@JsonProperty("role_id") String roleId, @JsonProperty("privileges") List<PrivilegeDetailResponse> privilegeDetailResponseList) {
}

