package com.bibliotech.security.service;


import com.bibliotech.security.dao.request.CreateRoleRequest;
import com.bibliotech.security.dao.request.CreateRoleResponse;
import com.bibliotech.security.dao.request.UpdateRoleRequest;

public interface RoleService  {
    
    CreateRoleResponse create(CreateRoleRequest createRolrequest);
    CreateRoleResponse update(UpdateRoleRequest updateRoleRequest);
}