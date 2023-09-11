package com.bibliotech.controller;

import com.bibliotech.entity.TipoPublicacion;
import com.bibliotech.service.TipoPublicacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/tipo_publicaciones")
public class TipoPublicacionController {

    private final TipoPublicacionService tipoPublicacionService;

    public TipoPublicacionController(TipoPublicacionService tipoPublicacionService) {
        this.tipoPublicacionService = tipoPublicacionService;
    }

    @GetMapping
    public List<TipoPublicacion> findAll(){
        return tipoPublicacionService.findAll();
    }
}
