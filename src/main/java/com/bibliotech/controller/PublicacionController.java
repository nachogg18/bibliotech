package com.bibliotech.controller;

import com.bibliotech.dto.*;
import com.bibliotech.entity.Publicacion;
import com.bibliotech.service.PublicacionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/publicaciones")
@Log4j2
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping(path = "/findByParams")
    public List<Publicacion> findByParams(@RequestBody FindPublicacionesByParamsDTO request) {

        request.validate();

        return publicacionService.findByParams(request);
    }

    @PostMapping(path = "")
    public Publicacion create(@RequestBody @Valid CreatePublicacionRequestDTO request) {
        
        return publicacionService.create(request);
    }

    @PostMapping("")
    public Publicacion save(@RequestBody Publicacion publicacion){
            return publicacionService.save(publicacion);
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
