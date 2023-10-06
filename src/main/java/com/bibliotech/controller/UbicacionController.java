package com.bibliotech.controller;

import com.bibliotech.entity.Ubicacion;
import com.bibliotech.service.UbicacionService;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/ubicaciones")
@SecurityRequirement(name = "bearer-key")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public List<Ubicacion> findAll() {
        return ubicacionService.findAll();
    }

    @PostMapping
    public Ubicacion post(@RequestBody Ubicacion ubicacion) {
        return ubicacionService.save(ubicacion);
    }

    @PutMapping("/{id}")
    public Ubicacion edit(@RequestBody Ubicacion ubicacion, @PathVariable Long id) {
        return ubicacionService.edit(ubicacion, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ubicacion> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ubicacionService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
