package com.bibliotech.controller;

import com.bibliotech.dto.BibliotecaCreateDTO;
import com.bibliotech.dto.BibliotecaDetalleDTO;
import com.bibliotech.dto.UbicacionResponseDTO;
import com.bibliotech.entity.Biblioteca;
import com.bibliotech.service.BibliotecaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Biblioteca>> findAll() {
        return ResponseEntity.ok().body(bibliotecaService.findAll());
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'BIBLIOTECA')")
    public ResponseEntity<BibliotecaDetalleDTO> save(@RequestBody BibliotecaCreateDTO biblioteca) {
        return ResponseEntity.ok().body(bibliotecaService.crearBiblioteca(biblioteca));
    }

    @PostMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'BIBLIOTECA')")
    public ResponseEntity<BibliotecaDetalleDTO> edit(@PathVariable Long id, @RequestBody BibliotecaCreateDTO biblioteca) {
        return ResponseEntity.ok().body(bibliotecaService.edit(id, biblioteca));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'BIBLIOTECA')")
    public ResponseEntity<BibliotecaDetalleDTO> delete(@PathVariable Long id) {
        return ResponseEntity.ok().body(bibliotecaService.delete(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'BIBLIOTECA')")
    public ResponseEntity<BibliotecaDetalleDTO> update(@PathVariable Long id ,@RequestBody BibliotecaCreateDTO biblioteca) {
        return ResponseEntity.ok().body(bibliotecaService.edit(id, biblioteca));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'BIBLIOTECA')")
    public ResponseEntity<BibliotecaDetalleDTO> findBibliotecaDetalle(@PathVariable Long id) {
        return ResponseEntity.ok().body(bibliotecaService.findBibliotecaDetalle(id));
    }

    @GetMapping("/{id}/ubicaciones")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'BIBLIOTECA')")
    public ResponseEntity<List<UbicacionResponseDTO>> findBibliotecaUbicaciones(@PathVariable Long id) {
        return ResponseEntity.ok().body(bibliotecaService.findBibliotecaUbicaciones(id));
    }
}
