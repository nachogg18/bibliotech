package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.CreateRoleRequest;
import com.bibliotech.security.dao.request.CreateRoleResponse;
import com.bibliotech.security.dao.request.UpdateRoleRequest;
import com.bibliotech.security.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController { 
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
   
    @Autowired
    private final RoleService roleService;
    
    @PostMapping("/create")
    public ResponseEntity<CreateRoleResponse> create(@RequestBody @Valid CreateRoleRequest request) {
        return ResponseEntity.ok(roleService.create(request));
    }

    @PutMapping ("/update")
    public ResponseEntity<CreateRoleResponse> update(@RequestBody @Valid UpdateRoleRequest request) {
        return ResponseEntity.ok(roleService.update(request));
    }
}