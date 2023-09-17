package com.bibliotech.security.service;


import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Role;
import java.util.List;
import java.util.Optional;

public interface PrivilegeService {

    List<Privilege> getAllPrivileges();

    Optional<Privilege> getPrivilegeById(Long id);

    Privilege savePrivilege(Privilege privilege);

    void addRoleToPrivilege(Long privilegeId, Role role);
}