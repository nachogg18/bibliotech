package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.service.RoleService;
import jakarta.validation.Valid;
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
    public ResponseEntity<GetPrivilegesFromRoleResponse> getPrivilegesForRoles(@RequestBody @Valid GetPrivilegesFromRoleRequest request) {


        var map = roleService.getPrivilegesFromRole(request.validateRequest().get(0));

        var privilegeSetToString = map.entrySet().stream().findAny().get().getValue().stream().map(privilege -> privilege.toString()).collect(Collectors.toSet());


        GetPrivilegesFromRoleResponse response = new GetPrivilegesFromRoleResponse(
                map.entrySet().stream().findAny().get().getValue().stream().map(privilege -> new GetPrivilegesFromRoleDetailResponse(
                        privilege.getId().toString(),
                        privilege.getName())
                ).collect(Collectors.toSet())
        );



        logger.info(response.toString());

        return ResponseEntity.ok(response);

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