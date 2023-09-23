package com.bibliotech.controller;

import com.bibliotech.dto.CrearEjemplarDTO;
import com.bibliotech.entity.Ejemplar;
import com.bibliotech.service.EjemplarService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/ejemplares")
@RequiredArgsConstructor
public class EjemplarController {

    private final EjemplarService ejemplarService;

    @PostMapping(path = "")
    public Ejemplar create(@RequestBody @Valid CrearEjemplarDTO request) throws Exception {
        return ejemplarService.createEjemplar(request);
    }
    @GetMapping
    public List<Ejemplar> findAll() {
        return ejemplarService.findAll();
    }

    @PutMapping("/{id}")
    public Ejemplar edit(@RequestBody Ejemplar ejemplar, @PathVariable Long id) {
        return ejemplarService.edit(ejemplar, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ejemplarService.delete(id);
    }

}
