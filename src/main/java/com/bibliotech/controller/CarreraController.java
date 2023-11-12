package com.bibliotech.controller;

import com.bibliotech.entity.Carrera;
import com.bibliotech.service.CarreraService;
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
@RequestMapping(path = "/api/v1/carreras")
@SecurityRequirement(name = "bearer-key")
@Log4j2
@RequiredArgsConstructor
public class CarreraController {

    private final CarreraService carreraService;

    @GetMapping(path = "")
   
    public List<Carrera> findAll() {
        return carreraService.findAll();
    }

    @PostMapping("")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'CARRERA')")
    public Carrera post(@RequestBody Carrera carrera) {
        return carreraService.save(carrera);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'CARRERA')")
    public Carrera edit(@RequestBody Carrera carrera, @PathVariable Long id) {
        return carreraService.edit(carrera, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'CARRERA')")
    public ResponseEntity<Carrera> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carreraService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
