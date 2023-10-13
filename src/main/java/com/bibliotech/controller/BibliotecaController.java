package com.bibliotech.controller;

import com.bibliotech.entity.Biblioteca;
import com.bibliotech.service.BibliotecaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bibliotecas")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class BibliotecaController {
    private final BibliotecaService bibliotecaService;

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'BIBLIOTECA')")
    public List<Biblioteca> findAll() {
        return bibliotecaService.findAll();
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'BIBLIOTECA')")
    public Biblioteca save(@RequestBody Biblioteca biblioteca) {
        return bibliotecaService.save(biblioteca);
    }

}
