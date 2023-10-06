package com.bibliotech.controller;

import com.bibliotech.dto.*;
import com.bibliotech.entity.Publicacion;
import com.bibliotech.service.PublicacionService;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/publicaciones")
@Log4j2
@SecurityRequirement(name = "bearer-key")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping(path = "/findByParams")
    public List<PublicacionResponseDTO> findAll(
            @RequestParam String parametro,
            @RequestParam(required = false) String contenido,
            @RequestBody List<BusquedaPublicacionCategoriaDTO> busquedaPublicacion
            ) {
        log.debug("(POST) Request to get PublicacionResponseDTO list");
        return publicacionService.findAllPublicacionDTO(parametro, contenido, busquedaPublicacion);
    }

    @GetMapping
    public List<Publicacion> findAll(){
        return publicacionService.findAll();
    }

    @GetMapping(path = "/paged")
    public ResponseEntity<PageDTO<PublicacionPaginadaDTO>> getAllPublicacionPaged(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.status(HttpStatus.OK).body(publicacionService.findAllPublicacionPaginatedDTO(page));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DetallePublicacionDTO> getDetallePublicacion(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(publicacionService.getDetallePublicacion(id));
    }

}
