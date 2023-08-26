package com.bibliotech.controller;

import com.bibliotech.dto.BusquedaPublicacionCategoriaDTO;
import com.bibliotech.dto.DetallePublicacionDTO;
import com.bibliotech.dto.PublicacionResponseDTO;
import com.bibliotech.service.PublicacionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/publicaciones")
@Log4j2
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping(path = "")
    public List<PublicacionResponseDTO> findAll(
            @RequestParam String parametro,
            @RequestParam(required = false) String contenido,
            @RequestBody(required = false) List<BusquedaPublicacionCategoriaDTO> busquedaPublicacion
            ) {
        log.debug("(POST) Request to get PublicacionResponseDTO list");
        return publicacionService.findAllPublicacionDTO(parametro, contenido, busquedaPublicacion);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DetallePublicacionDTO> getDetallePublicacion(@PathVariable Long id) {
        log.debug("(GET) Request to get DetallePublicacionDTO");
        return new ResponseEntity<>(publicacionService.getDetallePublicacion(id), HttpStatus.OK);
    }

}
