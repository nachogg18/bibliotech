package com.bibliotech.controller;


import com.bibliotech.entity.Comentario;
import com.bibliotech.service.ComentarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
