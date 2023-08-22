package com.bibliotech.controller;

import com.bibliotech.dto.DetallePublicacionDTO;
import com.bibliotech.dto.PublicacionResponseDTO;
import com.bibliotech.entity.Publicacion;
import com.bibliotech.service.PublicacionService;
import com.bibliotech.service.PublicacionServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/publicaciones")
@Log4j2
public class PublicacionController extends BaseControllerImpl<Publicacion, PublicacionServiceImpl> {

    @Autowired
    private PublicacionService publicacionService;

    @GetMapping(path = "/findAll")
    public List<PublicacionResponseDTO> findAll() {
        log.debug("Request to get all Publicacion");
        return publicacionService.findAllPublicacionDTO();
    }

    @GetMapping(path = "/findOne")
    public ResponseEntity<DetallePublicacionDTO> getDetallePublicacion() {
        return new ResponseEntity(null, HttpStatus.OK);
    }

}
