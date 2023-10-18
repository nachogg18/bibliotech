package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.service.ActionService;
import com.bibliotech.security.service.PrivilegeService;
import com.bibliotech.security.service.ResourceService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/privileges")
@RequiredArgsConstructor
public class PrivilegeController {
  private static final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);

  private final PrivilegeService privilegeService;

  private final ResourceService resourceService;

  private final ActionService actionService;

  @PostMapping("")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'PRIVILEGE')")
  @Secured("SUPERADMIN")
  public ResponseEntity<Privilege> create(@RequestBody @Valid CreatePrivilegeRequest request) {

    return ResponseEntity.ok(privilegeService.createPrivilege(request));
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
