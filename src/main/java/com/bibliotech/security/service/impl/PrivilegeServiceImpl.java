package com.bibliotech.security.service.impl;

import com.bibliotech.security.dao.request.CreatePrivilegeRequest;
import com.bibliotech.security.dao.request.EditPrivilegeRequest;
import com.bibliotech.security.entity.Action;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.repository.PrivilegeRepository;
import com.bibliotech.security.repository.RoleRepository;
import com.bibliotech.security.service.ActionService;
import com.bibliotech.security.service.PrivilegeService;
import com.bibliotech.security.service.ResourceService;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Transactional
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {

  private final PrivilegeRepository privilegeRepository;
  private final RoleRepository roleRepository;
  private final ResourceService resourceService;
  private final ActionService actionService;

  public List<Privilege> getAllPrivileges() {
    return privilegeRepository.findByEndDateNull();
  }

  public Optional<Privilege> getPrivilegeById(Long id) {
    return privilegeRepository.findById(id);
  }

  public Optional<Privilege> getPrivilegeByName(String name) {
    return privilegeRepository.findByName(name);
  }

  @Override
  public Privilege createPrivilege(CreatePrivilegeRequest request) {
    if (privilegeRepository.findByName(request.name()).isPresent()) {
      throw new ValidationException(String.format("ya existe un privilegio con nombre %s", request.name()));
    }

    Privilege privilegeToSave =
            Privilege.builder()
                    .name(request.name())
                    .resource(
                            resourceService
                                    .getResourcesById(request.resourceId())
                                    .orElseThrow(() -> new ValidationException("no existe recurso con ese id")))
                    .actions(
                            request.actions().stream()
                                    .map(
                                            requestActionName -> actionService.getActionByName(requestActionName).orElseThrow(() ->
                                                    new ValidationException(
                                                            "la acción no se encuentra disponible")))
                                    .collect(Collectors.toSet()))
                    .startDate(Instant.now())
                    .lastUpdatedDate(Instant.now())
                    .endDate(null)
                    .build();

    return privilegeRepository.save(privilegeToSave);
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

    if ((Objects.nonNull(request.enabled()) && request.enabled())) {
      privilege.setEndDate(Instant.now());
    } else {
      privilege.setEndDate(null);
    }

    privilege.setActions(actionSet);
    privilege.setLastUpdatedDate(Instant.now());

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

  public Privilege getOne(Long id){
    Optional<Privilege> isPrivilege = this.privilegeRepository.findByIdAndEndDateNull(id);
    if (isPrivilege.isPresent()) {
      return isPrivilege.get();
    } else {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Privilegio no encontrado");
    }
  }

  public boolean deleteOne(Long id){
    Privilege privilege = this.getOne(id);
    privilege.setEndDate(Instant.now());
    try {
      this.privilegeRepository.save(privilege);
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "No se puedo eliminar el privilegio");
    }
    return true;
  }
}
