package com.bibliotech.controller;

import com.bibliotech.entity.Edicion;
import com.bibliotech.service.EdicionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/ediciones")
@SecurityRequirement(name = "bearer-key")
@Log4j2
@RequiredArgsConstructor
public class EdicionController {

    private final EdicionService edicionService;

    @GetMapping(path = "/")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'EDICION')")
    public List<Edicion> findAll() {
        return edicionService.findAll();
    }

    @PostMapping("/")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'EDICION')")
    public Edicion post(@RequestBody Edicion edicion) {
        return edicionService.save(edicion);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'EDICION')")
    public Edicion edit(@RequestBody Edicion edicion, @PathVariable Long id) {
        return edicionService.edit(edicion, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'EDICION')")
    public ResponseEntity<Edicion> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(edicionService.delete(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
