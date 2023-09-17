package com.bibliotech.security.service;


import com.bibliotech.security.dao.request.CreateRoleRequest;
import com.bibliotech.security.dao.request.CreateRoleResponse;
import com.bibliotech.security.dao.request.UpdateRoleRequest;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import java.util.Optional;

public interface RoleService  {
    
    CreateRoleResponse create(CreateRoleRequest createRolrequest);
    CreateRoleResponse update(UpdateRoleRequest updateRoleRequest);
    
    Optional<Role> findByName(String RoleName);
    Role assignUserToRol(Long roleId, User user);
}