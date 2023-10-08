package com.bibliotech.security.service;


import com.bibliotech.security.dao.request.CreateRoleRequest;
import com.bibliotech.security.dao.request.CreateRoleResponse;
import com.bibliotech.security.dao.request.UpdateRoleRequest;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RoleService  {

    Map<Long, List<Privilege>> getPrivilegesFromRole(Long roleId);
    
    CreateRoleResponse create(CreateRoleRequest createRolrequest);
    Role create(Role role);
    CreateRoleResponse update(UpdateRoleRequest updateRoleRequest);
    Optional<Role> findById(Long roleId);
    Optional<Role> findByName(String RoleName);
    Role assignUserToRol(Long roleId, User user);
    Role removeUserToRol(Long roleId, User user);
    Role assignPrivilegeToRole(Long roleId, Privilege privilege);


}