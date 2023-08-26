package com.bibliotech.controller;

import com.bibliotech.dto.FiltroCategoriaDTO;
import com.bibliotech.service.CategoriaService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/categorias")
@Log4j2
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/filtros")
    public List<FiltroCategoriaDTO> findAllDTO() {
        log.debug("Request to get FiltroCategoriaDTO list");
        return categoriaService.findAllDTO();
    }


}
