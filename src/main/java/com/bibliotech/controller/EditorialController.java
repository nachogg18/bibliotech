package com.bibliotech.controller;

import com.bibliotech.entity.Editorial;
import com.bibliotech.service.EditorialService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/editoriales")
public class EditorialController {

    private final EditorialService editorialService;

    public EditorialController(EditorialService editorialService) {
        this.editorialService = editorialService;
    }

    @GetMapping
    public List<Editorial> findAll() {
        return editorialService.findAll();
    }

    @PostMapping
    public Editorial post(@RequestBody Editorial editorial) {
        return editorialService.save(editorial);
    }

    @PutMapping("/{id}")
    public Editorial edit(@RequestBody Editorial editorial, @PathVariable Long id) {
        return editorialService.edit(editorial, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Editorial> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(editorialService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
