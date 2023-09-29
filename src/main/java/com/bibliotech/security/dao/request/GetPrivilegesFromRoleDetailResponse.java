package com.bibliotech.security.dao.request;


import java.util.List;

public record GetPrivilegesFromRoleDetailResponse(String roleId, List<PrivilegeDetailResponse> privilegeDetailResponseList) {
}

