package com.bibliotech.controller;

import com.bibliotech.entity.TipoMulta;
import com.bibliotech.service.TipoMultaService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/tipo-multas")
public class TipoMultaController {

    private final TipoMultaService tipoMultaService;

    public TipoMultaController(TipoMultaService tipoMultaService) {
        this.tipoMultaService = tipoMultaService;
    }

    @GetMapping
    public List<TipoMulta> findAll() {
        return tipoMultaService.findAll();
    }

    @PostMapping
    public TipoMulta post(@RequestBody TipoMulta tipoMulta) {
        return tipoMultaService.save(tipoMulta);
    }

    @PutMapping("/{id}")
    public TipoMulta edit(@RequestBody TipoMulta tipoMulta, @PathVariable Long id) {
        return tipoMultaService.edit(tipoMulta, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TipoMulta> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tipoMultaService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
