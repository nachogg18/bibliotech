package com.bibliotech.controller;

import com.bibliotech.entity.Edicion;
import com.bibliotech.service.EdicionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/ediciones")
public class EdicionController {

    private final EdicionService edicionService;

    public EdicionController(EdicionService edicionService) {
        this.edicionService = edicionService;
    }

    @GetMapping(path = "/")
    public List<Edicion> findAll() {
        return edicionService.findAll();
    }
}
