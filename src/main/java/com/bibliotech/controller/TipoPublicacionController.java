package com.bibliotech.controller;

import com.bibliotech.entity.TipoPublicacion;
import com.bibliotech.service.TipoPublicacionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/tipo-publicaciones")
@SecurityRequirement(name = "bearer-key")
public class TipoPublicacionController {

    private final TipoPublicacionService tipoPublicacionService;

    public TipoPublicacionController(TipoPublicacionService tipoPublicacionService) {
        this.tipoPublicacionService = tipoPublicacionService;
    }

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'TIPO_PUBLICACION')")
    public List<TipoPublicacion> findAll() {
        return tipoPublicacionService.findAll();
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'TIPO_PUBLICACION')")
    public TipoPublicacion post(@RequestBody TipoPublicacion tipoPublicacion) {
        return tipoPublicacionService.save(tipoPublicacion);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'TIPO_PUBLICACION')")
    public TipoPublicacion edit(@RequestBody TipoPublicacion tipoPublicacion, @PathVariable Long id) {
        return tipoPublicacionService.edit(tipoPublicacion, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'TIPO_PUBLICACION')")
    public ResponseEntity<TipoPublicacion> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tipoPublicacionService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
