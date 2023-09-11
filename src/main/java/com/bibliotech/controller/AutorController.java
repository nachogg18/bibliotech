package com.bibliotech.controller;

import com.bibliotech.entity.Autor;
import com.bibliotech.service.AutorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping(path = "/")
    public List<Autor> findAll() {
        return autorService.findAll();
    }
}
