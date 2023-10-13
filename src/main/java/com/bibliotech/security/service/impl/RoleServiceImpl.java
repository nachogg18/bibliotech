package com.bibliotech.security.service.impl;


import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.repository.RoleRepository;
import com.bibliotech.security.service.PrivilegeService;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.utils.PrivilegeUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeService privilegeService;


    @Override
    public Map<Long, List<Privilege>> getPrivilegesFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        
        if (role.isEmpty()) {
            logger.error(String.format("El rol con id %s no existe", roleId));
            return Map.of(roleId, List.of());
        }
        
        var map = Map.of(roleId, role.get().getPrivileges().stream().sorted(Comparator.comparingLong(Privilege::getId)).collect(Collectors.toList()));
        return map;
    }

    @Override
    public CreateRoleResponse create(CreateRoleRequest createRolerequest) {
        if (!roleRepository.findByNameAndEndDateNull(createRolerequest.name()).isEmpty()) {
            throw  new RuntimeException("El rol ya existe");
        }

    Role role = Role.builder()
            .name(createRolerequest.name())
            .build();

        role = roleRepository.save(role);

        Role finalRole = role;
        
        if (createRolerequest.privilegesIdsToAssign() == null || createRolerequest.privilegesIdsToAssign().isEmpty()) {
            Optional<Privilege> privilege = privilegeService.getPrivilegeByName(PrivilegeUtils.DEFAULT_PRIVILEGE);
            if (privilege.isPresent()) {
                assignPrivilegeToRole(finalRole.getId(), privilege.get()); 
            }
            
        } else {
            createRolerequest.privilegesIdsToAssign().stream()
                    .peek(
                            privilegeId -> {
                                Privilege privilege = privilegeService.addRoleToPrivilege(privilegeId, finalRole);
                                assignPrivilegeToRole(finalRole.getId(), privilege);
                            });
        }
        
        return CreateRoleResponse.fromRole(role);
    }

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
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
    public Optional<Role> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public Optional<Role> findByIdAndEndDateNull(Long roleId) {
        return roleRepository.findByIdAndEndDateNull(roleId);
    }

    @Override
    public Optional<Role> findByNameAndEndDateNull(String roleName) {
        return roleRepository.findByNameAndEndDateNull(roleName);
    }

    @Override
    public Optional<Role> findByName(String  roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    public Role assignUserToRol(Long roleId, User user) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            throw new RuntimeException("El rol no existe");
        }
        role.get().getUsers().add(user);
        return roleRepository.save(role.get());
    }

    @Override
    public Role removeUserToRol(Long roleId, User user) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            throw new RuntimeException("El rol no existe");
        }
        Boolean isRemoved = role.get().getUsers().remove(user);
        
        if (!isRemoved) {
            throw new ValidationException("hubo un problema removiendo al usuario del rol");
        }
        return roleRepository.save(role.get());
    }

    @Override
    public Role assignPrivilegeToRole(Long roleId, Privilege privilege) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            throw new RuntimeException("El rol no existe");
        }
        role.get().getPrivileges().add(privilege);
        return roleRepository.save(role.get());
    }

    @Override
    public CreateRoleResponse delete(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.get().setEndDate(Instant.now());
        roleRepository.save(role.get());
        return new CreateRoleResponse(role.get().getId().toString(), role.get().getName(), role.get().getStartDate().toString(), role.get().getEndDate().toString());
    }
}
