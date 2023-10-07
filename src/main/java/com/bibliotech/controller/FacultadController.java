package com.bibliotech.controller;

import com.bibliotech.entity.Facultad;
import com.bibliotech.service.FacultadService;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/facultades")
@SecurityRequirement(name = "bearer-key")
public class FacultadController {

    private final FacultadService facultadService;

    public FacultadController(FacultadService facultadService) {
        this.facultadService = facultadService;
    }

    @GetMapping
    public List<Facultad> findAll() {
        return facultadService.findAll();
    }

    @PostMapping
    public Facultad post(@RequestBody Facultad facultad) {
        return facultadService.save(facultad);
    }

    @PutMapping("/{id}")
    public Facultad edit(@RequestBody Facultad facultad, @PathVariable Long id) {
        return facultadService.edit(facultad, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Facultad> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(facultadService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
