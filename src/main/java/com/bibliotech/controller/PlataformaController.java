package com.bibliotech.controller;

import com.bibliotech.entity.Plataforma;
import com.bibliotech.service.PlataformaService;
import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/plataformas")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearer-key")
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

    @PostMapping
    public ResponseEntity<Plataforma> post(@RequestBody Plataforma plataforma) {
        return ResponseEntity.status(HttpStatus.OK).body(plataformaService.save(plataforma));
    }

}
