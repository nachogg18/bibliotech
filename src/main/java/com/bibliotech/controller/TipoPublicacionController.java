package com.bibliotech.controller;

import com.bibliotech.entity.TipoPublicacion;
import com.bibliotech.service.TipoPublicacionService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/tipo-publicaciones")
public class TipoPublicacionController {

    private final TipoPublicacionService tipoPublicacionService;

    public TipoPublicacionController(TipoPublicacionService tipoPublicacionService) {
        this.tipoPublicacionService = tipoPublicacionService;
    }

    @GetMapping
    public List<TipoPublicacion> findAll() {
        return tipoPublicacionService.findAll();
    }

    @PostMapping
    public TipoPublicacion post(@RequestBody TipoPublicacion tipoPublicacion) {
        return tipoPublicacionService.save(tipoPublicacion);
    }

    @PutMapping("/{id}")
    public TipoPublicacion edit(@RequestBody TipoPublicacion tipoPublicacion, @PathVariable Long id) {
        return tipoPublicacionService.edit(tipoPublicacion, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TipoPublicacion> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tipoPublicacionService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
