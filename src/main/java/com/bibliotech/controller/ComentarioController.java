package com.bibliotech.controller;


import com.bibliotech.dto.ComentarioDTO;
import com.bibliotech.dto.CrearComentarioDTO;
import com.bibliotech.entity.Comentario;
import com.bibliotech.service.ComentarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/comentarios")
@SecurityRequirement(name = "bearer-key")
public class ComentarioController {

    @Autowired
    ComentarioService comentarioService;

    @DeleteMapping(path = "/{id}")
    //TODO: Agregar autorizacion para eliminar comentario
    public ResponseEntity<Comentario> delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(comentarioService.delete(id));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<ComentarioDTO>> getComentariosByUser(@PathVariable Long id) {
        return ResponseEntity.ok().body(comentarioService.findByUserId(id));
    }

    @GetMapping("/ejemplar/{id}")
    public ResponseEntity<List<ComentarioDTO>> getComentariosByEjemplar(@PathVariable Long id) {
        return ResponseEntity.ok().body(comentarioService.findByEjemplarId(id));
    }

    @GetMapping("/publicacion/{id}")
    public ResponseEntity<List<ComentarioDTO>> getComentariosByPublicacion(@PathVariable Long id) {
        return ResponseEntity.ok().body(comentarioService.findByPublicacionId(id));
    }

    @GetMapping("")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<List<ComentarioDTO>> getComentarios() {
        return ResponseEntity.ok().body(comentarioService.findAll());
    }

    @PostMapping("/ejemplar/{idEjemplar}")
    public ResponseEntity<ComentarioDTO> crearComentarioEjemplar(@RequestBody CrearComentarioDTO crearComentarioDTO, @PathVariable Long idEjemplar){
        return ResponseEntity.ok().body(comentarioService.saveComentarioEjemplar(crearComentarioDTO, idEjemplar));
    }

    @PostMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<ComentarioDTO> crearComentarioPublicacion(@RequestBody CrearComentarioDTO crearComentarioDTO, @PathVariable Long idPublicacion){
        return ResponseEntity.ok().body(comentarioService.saveComentarioPublicacion(crearComentarioDTO, idPublicacion));
    }
}
