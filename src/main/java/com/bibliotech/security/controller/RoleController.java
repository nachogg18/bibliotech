package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/roles")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class RoleController {
  private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

  @Autowired private final RoleService roleService;

  @PostMapping("/get-privileges-for-roles")
  //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'ROLE')")
  public ResponseEntity<List<GetPrivilegesFromRoleDetailResponse>> getPrivilegesForRoles(
      @RequestBody @Valid GetPrivilegesFromRoleRequest request) {

    return ResponseEntity.ok(
        request.validateRequest().stream()
            .map(roleService::getPrivilegesFromRole)
            .flatMap(
                m ->

                        m.entrySet().stream()
                            .map(
                                entry ->
                                    new GetPrivilegesFromRoleDetailResponse(
                                        entry.getKey().toString(),
                                        entry.getValue().stream()
                                            .map(
                                                privilege ->
                                                    new PrivilegeDetailResponse(
                                                        privilege.getId().toString(),
                                                        privilege.getName()))
                                            .collect(Collectors.toList())))
            ).toList());
  }

  @GetMapping()
//  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'ROLE')")
  public ResponseEntity getPrivilegesForRoles() {
    return ResponseEntity.ok(roleService.getRoles());
  }

  @GetMapping("/{id}")
  //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'ROLE')")
  public ResponseEntity getOne(@PathVariable Long id) { return ResponseEntity.ok(roleService.findOne(id)); }

  @PostMapping("/create")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'ROLE')")
  @Secured("SUPERADMIN")
  public ResponseEntity<CreateRoleResponse> create(@RequestBody @Valid CreateRoleRequest request) {
    return ResponseEntity.ok(roleService.create(request));
  }

  @PutMapping("/update")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'ROLE')")
  public ResponseEntity<CreateRoleResponse> update(@RequestBody @Valid UpdateRoleRequest request) {
    return ResponseEntity.ok(roleService.update(request));
  }

  @DeleteMapping("/delete/{roleId}")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'ROLE')")
  public ResponseEntity<CreateRoleResponse> delete(@PathVariable Long roleId) {
    return ResponseEntity.ok(roleService.delete(roleId));
  }
}
