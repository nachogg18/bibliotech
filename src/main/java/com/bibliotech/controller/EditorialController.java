package com.bibliotech.controller;

import com.bibliotech.entity.Editorial;
import com.bibliotech.service.EditorialService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/editoriales")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class EditorialController {

    private final EditorialService editorialService;

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'EDITORIAL')")
    public List<Editorial> findAll() {
        return editorialService.findAll();
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'EDITORIAL')")
    public Editorial post(@RequestBody Editorial editorial) {
        return editorialService.save(editorial);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'EDITORIAL')")
    public Editorial edit(@RequestBody Editorial editorial, @PathVariable Long id) {
        return editorialService.edit(editorial, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'EDITORIAL')")
    public ResponseEntity<Editorial> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(editorialService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
