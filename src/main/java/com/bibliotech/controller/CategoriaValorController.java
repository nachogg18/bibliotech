package com.bibliotech.controller;

import com.bibliotech.dto.CrearValorDTO;
import com.bibliotech.dto.MostrarCategoriaValorDTO;
import com.bibliotech.entity.CategoriaValor;
import com.bibliotech.service.CategoriaValorService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/categoria-valores")
public class CategoriaValorController {

    private final CategoriaValorService categoriaValorService;

    public CategoriaValorController(CategoriaValorService categoriaValorService) {
        this.categoriaValorService = categoriaValorService;
    }

    @GetMapping
    public List<CategoriaValor> findAll() {
        return categoriaValorService.findAll();
    }

    @PostMapping
    public ResponseEntity<MostrarCategoriaValorDTO> post(@RequestBody CrearValorDTO valorDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaValorService.save(valorDTO));
    }

    @PutMapping("/{id}")
    public CategoriaValor edit(@RequestBody CategoriaValor categoriaValor, @PathVariable Long id) {
        return categoriaValorService.edit(categoriaValor, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoriaValor> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaValorService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
