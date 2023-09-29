package com.bibliotech.security.dao.request;


import java.util.Set;

public record GetPrivilegesFromRoleResponse(Set<GetPrivilegesFromRoleDetailResponse> details) {
}

