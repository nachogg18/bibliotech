package com.bibliotech.security.service.impl;

import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.repository.PrivilegeRepository;
import com.bibliotech.security.repository.RoleRepository;
import com.bibliotech.security.service.PrivilegeService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository;
    

    public List<Privilege> getAllPrivileges() {
        return privilegeRepository.findAll();
    }

    public Optional<Privilege> getPrivilegeById(Long id) {
        return privilegeRepository.findById(id);
    }

    public Privilege savePrivilege(Privilege privilege) {
        return privilegeRepository.save(privilege);
    }

    public void addRoleToPrivilege(Long privilegeId, Role role) {
        Optional<Privilege> privilegeOptional = privilegeRepository.findById(privilegeId);

        if (privilegeOptional.isPresent()) {
            Privilege privilege = privilegeOptional.get();

            // Add the Role to the Privilege
            privilege.getRoles().add(role);
            privilegeRepository.save(privilege);
        }
    }
}
