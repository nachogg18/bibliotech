package com.bibliotech.controller;

import com.bibliotech.entity.Autor;
import com.bibliotech.service.AutorService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/api/v1/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public List<Autor> findAll() {
        return autorService.findAll();
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOf('WRITE_AUTOR')")
    public Autor post(@RequestBody Autor autor) {
        return autorService.save(autor);
    }

    @PutMapping("/{id}")
    public Autor edit(@RequestBody Autor autor, @PathVariable Long id) {
        return autorService.edit(autor, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Autor> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(autorService.delete(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")));
    }

}
