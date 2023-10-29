package com.bibliotech.controller;

import com.bibliotech.entity.Ubicacion;
import com.bibliotech.service.UbicacionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/ubicaciones")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'UBICACION')")
    public List<Ubicacion> findAll() {
        return ubicacionService.findAll();
    }

    @GetMapping("disponibles")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'UBICACION')")
    public List<Ubicacion> findAllDisponibles() {
        return ubicacionService.findAllDispobles();
    }

    @GetMapping("disponibles-with/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'UBICACION')")
    public List<Ubicacion> findAllDisponiblesWith(@PathVariable Long id) {
        return ubicacionService.findAllDispoblesWith(id);
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'UBICACION')")
    public Ubicacion post(@RequestBody Ubicacion ubicacion) {
        return ubicacionService.save(ubicacion);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'UBICACION')")
    public Ubicacion edit(@RequestBody Ubicacion ubicacion, @PathVariable Long id) {
        return ubicacionService.edit(ubicacion, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'UBICACION')")
    public ResponseEntity<Ubicacion> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ubicacionService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
