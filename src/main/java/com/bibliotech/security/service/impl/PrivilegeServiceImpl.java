package com.bibliotech.security.service.impl;

import com.bibliotech.security.dao.request.EditPrivilegeRequest;
import com.bibliotech.security.entity.Action;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.repository.PrivilegeRepository;
import com.bibliotech.security.repository.RoleRepository;
import com.bibliotech.security.service.PrivilegeService;
import jakarta.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {

  @Autowired private PrivilegeRepository privilegeRepository;

  @Autowired private RoleRepository roleRepository;

  public List<Privilege> getAllPrivileges() {
    return privilegeRepository.findAll();
  }

  public Optional<Privilege> getPrivilegeById(Long id) {
    return privilegeRepository.findById(id);
  }

  public Optional<Privilege> getPrivilegeByName(String name) {
    return privilegeRepository.findByName(name);
  }

  public Privilege savePrivilege(Privilege privilege) {
    return privilegeRepository.save(privilege);
  }

  public Privilege editPrivilege(EditPrivilegeRequest request) {

    Privilege privilege =
        this.getPrivilegeById(request.id())
            .orElseThrow(
                () ->
                    new ValidationException(
                        String.format("no existe el privilegio con id %s", request.id())));

    if (Objects.nonNull(request.name())) {
      privilege.setName(request.name());
    }

    Set<Action> actionSet =
        request.actions().stream()
            .map(
                requestActionName ->
                    Arrays.stream(Action.values())
                        .filter(actionName -> actionName.equals(requestActionName))
                        .findAny()
                        .orElseThrow(
                            () ->
                                new ValidationException(
                                    String.format(
                                        "la acción %s no se encuentra disponible",
                                        requestActionName))))
            .collect(Collectors.toSet());

    privilege.setActions(actionSet);

    return privilegeRepository.save(privilege);
  }

  public Privilege addRoleToPrivilege(Long privilegeId, Role role) {
    Optional<Privilege> privilegeOptional = privilegeRepository.findById(privilegeId);

    if (privilegeOptional.isPresent()) {
      Privilege privilege = privilegeOptional.get();

      // Add the Role to the Privilege
      privilege.getRoles().add(role);
      return privilegeRepository.save(privilege);
    } else {
      throw new RuntimeException("Error en la asignación de rol");
    }
  }
}
