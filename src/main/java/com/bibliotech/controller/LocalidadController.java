package com.bibliotech.controller;

import com.bibliotech.dto.LocalidadDTO;
import com.bibliotech.service.LocalidadService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/localidades")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class LocalidadController {

    private final LocalidadService localidadService;

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'LOCALIDAD')")
    public ResponseEntity<List<LocalidadDTO>> findAll() {
        return ResponseEntity.ok(localidadService.findByFechaBajaNull().stream().map(
                LocalidadDTO::toDto
        ).toList());
    }

}
