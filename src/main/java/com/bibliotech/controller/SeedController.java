package com.bibliotech.controller;


import com.bibliotech.service.SeedService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/seed")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class SeedController {

    private final SeedService seedService;

    @GetMapping(path = "autores")
    public ResponseEntity<String> cargarAutores() {
        return ResponseEntity.ok().body(seedService.cargarAutores());
    }

    @GetMapping(path = "bibliotecas")
    public ResponseEntity<String> cargarBibliotecas() {
        return ResponseEntity.ok().body(seedService.cargarBilbiotecas());
    }

    @GetMapping(path = "carreras")
    public ResponseEntity<String> cargarCarreras() {
        return ResponseEntity.ok().body(seedService.cargarCarreras());
    }

    @GetMapping(path = "categorias")
    public ResponseEntity<String> cargarCategorias() {
        return ResponseEntity.ok().body(seedService.cargarCategorias());
    }

    @GetMapping(path = "valores")
    public ResponseEntity<String> cargarValores() {
        return ResponseEntity.ok().body(seedService.cargarValores());
    }

    @GetMapping(path = "ediciones")
    public ResponseEntity<String> cargarEdiciones() {
        return ResponseEntity.ok().body(seedService.cargarEdiciones());
    }

    @GetMapping(path = "editoriales")
    public ResponseEntity<String> cargarEditoriales() {
        return ResponseEntity.ok().body(seedService.cargarEditoriales());
    }

    @GetMapping(path = "facultades")
    public ResponseEntity<String> cargarFacultades() {
        return ResponseEntity.ok().body(seedService.cargarFacultades());
    }

    @GetMapping(path = "plataformas")
    public ResponseEntity<String> cargarPlataformas() {
        return ResponseEntity.ok().body(seedService.cargarPlataformas());
    }

    @GetMapping(path = "tipos-publicacion")
    public ResponseEntity<String> cargarTiposPublicacion() {
        return ResponseEntity.ok().body(seedService.cargarTiposPublicacion());
    }

    @GetMapping(path = "tipos-multa")
    public ResponseEntity<String> cargarTiposMulta() {
        return ResponseEntity.ok().body(seedService.cargarTiposMulta());
    }

    @GetMapping(path = "publicacion")
    public ResponseEntity<String> cargarPublicaciones() {
        return ResponseEntity.ok().body(seedService.cargarPublicaciones());
    }

    @GetMapping(path = "ubicacion")
    public ResponseEntity<String> cargarUbicaciones() {
        return ResponseEntity.ok().body(seedService.cargarUbicaciones());
    }

    @GetMapping(path = "usuario")
    public ResponseEntity<String> cargarUsuarios() {
        return ResponseEntity.ok().body(seedService.cargarUsuarios());
    }

    @GetMapping(path = "ejemplar")
    public ResponseEntity<String> cargarEjemplares() {
        return ResponseEntity.ok().body(seedService.cargarEjemplares());
    }
    @GetMapping(path = "parametro")
    public ResponseEntity<String> cargarParametros() {
        return ResponseEntity.ok().body(seedService.cargarParametros());
    }
    @GetMapping(path = "pais")
    public ResponseEntity<String> cargarPaises() {
        return ResponseEntity.ok().body(seedService.cargarPaises());
    }
    @GetMapping(path = "provincia")
    public ResponseEntity<String> cargarProvincias() {
        return ResponseEntity.ok().body(seedService.cargarProvincias());
    }
    @GetMapping(path = "localidad")
    public ResponseEntity<String> cargarLocalidades() {
        return ResponseEntity.ok().body(seedService.cargarLocalidades());
    }

    @GetMapping(path = "carga-masiva")
    public ResponseEntity<String> cargaMasiva() {
        return ResponseEntity.ok().body(seedService.cargaMasiva());
    }
}
