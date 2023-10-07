package com.bibliotech.controller;

import com.bibliotech.dto.*;
import com.bibliotech.entity.Categoria;
import com.bibliotech.service.CategoriaService;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/categorias")
@SecurityRequirement(name = "bearer-key")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<MostrarCategoriaDTO> findAll() {
        return categoriaService.findAll();
    }

    @PostMapping
    public ResponseEntity<MostrarCategoriaDTO> post(@RequestBody CrearCategoriaDTO categoriaDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.save(categoriaDTO));
    }

    @PutMapping("/{id}")
    public MostrarCategoriaValorDTO edit(@RequestBody Categoria categoria, @PathVariable Long id) {
        return categoriaService.edit(categoria, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Categoria> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

    @GetMapping("/filtros")
    public List<FiltroCategoriaDTO> findAllDTO() {
        return categoriaService.findAllDTO();
    }


}
