package com.bibliotech.security.service;


import com.bibliotech.security.dao.request.CreatePrivilegeRequest;
import com.bibliotech.security.dao.request.EditPrivilegeRequest;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Role;
import java.util.List;
import java.util.Optional;

public interface PrivilegeService {

    List<Privilege> getAllPrivileges();

    Optional<Privilege> getPrivilegeById(Long id);

    Optional<Privilege> getPrivilegeByName(String name);

    Privilege createPrivilege(CreatePrivilegeRequest createPrivilegeRequest);

    Privilege savePrivilege(Privilege privilege);

    Privilege addRoleToPrivilege(Long privilegeId, Role role);

    Privilege editPrivilege(EditPrivilegeRequest editPrivilegeRequest);

    Privilege getOne(Long id);

    boolean deleteOne(Long id);
}