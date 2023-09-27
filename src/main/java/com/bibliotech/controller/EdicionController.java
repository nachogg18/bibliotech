package com.bibliotech.controller;

import com.bibliotech.entity.Edicion;
import com.bibliotech.service.EdicionService;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/ediciones")
@Log4j2
public class EdicionController {

    private final EdicionService edicionService;

    public EdicionController(EdicionService edicionService) {
        this.edicionService = edicionService;
    }

    @GetMapping(path = "/")
    public List<Edicion> findAll() {
        return edicionService.findAll();
    }

    @PostMapping("/")
    public Edicion post(@RequestBody Edicion edicion) {
        return edicionService.save(edicion);
    }

    @PutMapping("/{id}")
    public Edicion edit(@RequestBody Edicion edicion, @PathVariable Long id) {
        return edicionService.edit(edicion, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Edicion> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(edicionService.delete(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
