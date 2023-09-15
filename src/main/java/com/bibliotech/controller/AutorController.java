package com.bibliotech.controller;

import com.bibliotech.entity.Autor;
import com.bibliotech.service.AutorService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/autores/")
@CrossOrigin(origins = "*")
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
    public Autor save(@RequestBody Autor autor) {
        return autorService.save(autor);
    }

    @PutMapping("/{id}")
    public Autor edit(@RequestBody Autor autor, @PathVariable Long id) {
        return autorService.edit(autor, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        autorService.delete(id);
    }

}
