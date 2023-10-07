package com.bibliotech.controller;

import com.bibliotech.dto.CrearValorDTO;
import com.bibliotech.dto.MostrarCategoriaValorDTO;
import com.bibliotech.entity.CategoriaValor;
import com.bibliotech.service.CategoriaValorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/categoria-valores")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class CategoriaValorController {

    private final CategoriaValorService categoriaValorService;

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'CATEGORIA_VALOR')")
    public List<CategoriaValor> findAll() {
        return categoriaValorService.findAll();
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'CATEGORIA_VALOR')")
    public ResponseEntity<MostrarCategoriaValorDTO> post(@RequestBody CrearValorDTO valorDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaValorService.save(valorDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'CATEGORIA_VALOR')")
    public CategoriaValor edit(@RequestBody CategoriaValor categoriaValor, @PathVariable Long id) {
        return categoriaValorService.edit(categoriaValor, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'CATEGORIA_VALOR')")
    public ResponseEntity<CategoriaValor> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaValorService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
