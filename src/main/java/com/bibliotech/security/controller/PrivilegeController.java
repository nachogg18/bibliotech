package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.entity.Action;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.service.PrivilegeService;
import com.bibliotech.security.service.ResourceService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/privileges")
@RequiredArgsConstructor
public class PrivilegeController {
  private static final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);

  @Autowired private final PrivilegeService privilegeService;

  @Autowired private final ResourceService resourceService;

  @PostMapping("/create")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'PRIVILEGE')")
  @Secured("SUPERADMIN")
  public ResponseEntity<Privilege> create(@RequestBody @Valid CreatePrivilegeRequest request) {

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
                        requestActionName ->
                            Arrays.stream(Action.values())
                                .filter(actionName -> actionName.equals(requestActionName))
                                .findAny()
                                .orElseThrow(
                                    () ->
                                        new ValidationException(
                                            "la acción no se encuentra disponible")))
                    .collect(Collectors.toSet()))
            .build();

    return ResponseEntity.ok(privilegeService.savePrivilege(privilegeToSave));
  }

  @PostMapping("/edit")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRIVILEGE')")
  @Secured("SUPERADMIN")
  public ResponseEntity<Privilege> edit(@RequestBody @Valid EditPrivilegeRequest request) {

    return ResponseEntity.ok(privilegeService.editPrivilege(request));
  }

  @GetMapping("")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PRIVILEGE')")
  @Secured("SUPERADMIN")
  public ResponseEntity<List<Privilege>> getAll() {

    return ResponseEntity.ok(privilegeService.getAllPrivileges());
  }
}
