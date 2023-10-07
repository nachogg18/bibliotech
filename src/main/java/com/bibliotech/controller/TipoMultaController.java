package com.bibliotech.controller;

import com.bibliotech.entity.TipoMulta;
import com.bibliotech.service.TipoMultaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/tipo-multas")
@SecurityRequirement(name = "bearer-key")
public class TipoMultaController {

    private final TipoMultaService tipoMultaService;

    public TipoMultaController(TipoMultaService tipoMultaService) {
        this.tipoMultaService = tipoMultaService;
    }

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'TIPO_MULTA')")
    public List<TipoMulta> findAll() {
        return tipoMultaService.findAll();
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'TIPO_MULTA')")
    public TipoMulta post(@RequestBody TipoMulta tipoMulta) {
        return tipoMultaService.save(tipoMulta);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'TIPO_MULTA')")
    public TipoMulta edit(@RequestBody TipoMulta tipoMulta, @PathVariable Long id) {
        return tipoMultaService.edit(tipoMulta, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'TIPO_MULTA')")
    public ResponseEntity<TipoMulta> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tipoMultaService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
