package com.bibliotech.controller;

import com.bibliotech.dto.*;
import com.bibliotech.entity.Categoria;
import com.bibliotech.service.CategoriaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/categorias")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'CATEGORIA')")
    public List<MostrarCategoriaDTO> findAll() {
        return categoriaService.findAll();
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'CATEGORIA')")
    public ResponseEntity<MostrarCategoriaDTO> post(@RequestBody CrearCategoriaDTO categoriaDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.save(categoriaDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'CATEGORIA')")
    public MostrarCategoriaValorDTO edit(@RequestBody Categoria categoria, @PathVariable Long id) {
        return categoriaService.edit(categoria, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'CATEGORIA')")
    public ResponseEntity<Categoria> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

    @GetMapping("/filtros")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
    public List<FiltroCategoriaDTO> findAllDTO() {
        return categoriaService.findAllDTO();
    }


}
