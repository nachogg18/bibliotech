package com.bibliotech.controller;

import com.bibliotech.entity.Plataforma;
import com.bibliotech.service.PlataformaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/plataformas")
@CrossOrigin(origins = "*")
public class PlataformaController {

    private final PlataformaService plataformaService;

    public PlataformaController(PlataformaService plataformaService) {
        this.plataformaService = plataformaService;
    }

    @GetMapping
    public List<Plataforma> findAll() {
        return plataformaService.findAll();
    }

    @PutMapping("/{id}")
    public Plataforma edit(@RequestBody Plataforma plataforma, @PathVariable Long id) {
        return plataformaService.edit(plataforma, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        plataformaService.delete(id);
    }

}
