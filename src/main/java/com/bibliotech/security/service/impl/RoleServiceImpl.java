package com.bibliotech.security.service.impl;


import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.repository.RoleRepository;
import com.bibliotech.security.service.RoleService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    
    @Transactional
    @Override
    public CreateRoleResponse create(CreateRoleRequest createRolerequest) {
        if (!roleRepository.findByName(createRolerequest.name()).isEmpty()) {
            throw  new RuntimeException("El rol ya existe");
        }
        
        Role role = new Role(createRolerequest.name());
        
        
        role = roleRepository.save(role);

        return CreateRoleResponse.fromRole(role);
    }

    @Override
    public CreateRoleResponse update(UpdateRoleRequest updateRoleRequest) {

        Optional<Role> existingRole = roleRepository.findById(updateRoleRequest.id());
        
        if (existingRole.isEmpty()) {
            throw  new RuntimeException("No se encontró el rol");
        }

        Role role = roleRepository.save(updateRoleRequest.toRole(existingRole.get()));

        return CreateRoleResponse.fromRole(role);
    }

    @Override
    public Optional<Role> findByName(String  roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    public Role assignUserToRol(Role role, User user) {
        role.setUsers(Stream.concat(role.getUsers().stream(), Stream.of(user)).toList());
        return roleRepository.save(role);
    }

    


}
