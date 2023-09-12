package com.bibliotech.controller;

import com.bibliotech.entity.Edicion;
import com.bibliotech.service.EdicionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/ediciones")
@CrossOrigin(origins = "*")
public class EdicionController {

    private final EdicionService edicionService;

    public EdicionController(EdicionService edicionService) {
        this.edicionService = edicionService;
    }

    @GetMapping(path = "/")
    public List<Edicion> findAll() {
        return edicionService.findAll();
    }

    @PutMapping("/{id}")
    public Edicion edit(@RequestBody Edicion edicion, @PathVariable Long id) {
        return edicionService.edit(edicion, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        edicionService.delete(id);
    }

}
