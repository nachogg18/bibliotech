package com.bibliotech.controller;

import com.bibliotech.entity.Plataforma;
import com.bibliotech.service.PlataformaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/plataformas")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class PlataformaController {

    private final PlataformaService plataformaService;

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PLATAFORMA')")
    public List<Plataforma> findAll() {
        return plataformaService.findAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PLATAFORMA')")
    public Plataforma edit(@RequestBody Plataforma plataforma, @PathVariable Long id) {
        return plataformaService.edit(plataforma, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'PLATAFORMA')")
    public void delete(@PathVariable Long id) {
        plataformaService.delete(id);
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'PLATAFORMA')")
    public ResponseEntity<Plataforma> post(@RequestBody Plataforma plataforma) {
        return ResponseEntity.status(HttpStatus.OK).body(plataformaService.save(plataforma));
    }

}
