package com.bibliotech.controller;

import com.bibliotech.entity.Autor;
import com.bibliotech.service.AutorService;
import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/autores")
@SecurityRequirement(name = "bearer-key")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'AUTOR')")
    public List<Autor> findAll() {
        return autorService.findAll();
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'AUTOR')")
    public Autor post(@RequestBody Autor autor) {
        return autorService.save(autor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'AUTOR')")
    public Autor edit(@RequestBody Autor autor, @PathVariable Long id) {
        return autorService.edit(autor, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'AUTOR')")
    public ResponseEntity<Autor> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(autorService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
