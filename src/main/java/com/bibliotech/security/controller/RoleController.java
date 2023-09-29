package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.service.RoleService;
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
@RequiredArgsConstructor
public class RoleController { 
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
   
    @Autowired
    private final RoleService roleService;

   /* @PostMapping ("/get-privileges-for-roles")
    @PreAuthorize("@authenticationService.hasAdminRole()")
    public ResponseEntity<List<Map<Long, Set<Privilege>>>> getPrivilegesForRoles(@RequestBody @Valid GetPrivilegesFromRoleRequest request) {

        return ResponseEntity.ok(request.validateRequest().stream().map(
                roleId -> roleService.getPrivilegesFromRole(roleId)
        ).collect(Collectors.toList()));
    }*/

    @PostMapping ("/get-privileges-for-roles")
    @PreAuthorize("@authenticationService.hasAdminRole()")
    public ResponseEntity<List<GetPrivilegesFromRoleResponse>> getPrivilegesForRoles(@RequestBody @Valid GetPrivilegesFromRoleRequest request) {

        return ResponseEntity.ok(

        request.validateRequest().stream().map(
                roleService::getPrivilegesFromRole
        ).map(
                m -> 
                new GetPrivilegesFromRoleResponse(

                        m.entrySet().stream().map(
                                entry ->
                                        new GetPrivilegesFromRoleDetailResponse(
                                                entry.getKey().toString(),
                                                entry.getValue().stream().map(
                                                        privilege -> new PrivilegeDetailResponse(
                                                                privilege.getId().toString(),
                                                                privilege.getName()
                                                        )
                                                ).collect(Collectors.toList())
                                        )
                        ).collect(Collectors.toSet()))
        ).collect(Collectors.toList())
    );  


    }
    
    @PostMapping("/create")
    @PreAuthorize("@authenticationService.hasAdminRole()")
    @Secured("SUPERADMIN")
    public ResponseEntity<CreateRoleResponse> create(@RequestBody @Valid CreateRoleRequest request) {
        return ResponseEntity.ok(roleService.create(request));
    }

    @PutMapping ("/update")
    @PreAuthorize("@authenticationService.hasAdminRole()")
    public ResponseEntity<CreateRoleResponse> update(@RequestBody @Valid UpdateRoleRequest request) {
        return ResponseEntity.ok(roleService.update(request));
    }


}