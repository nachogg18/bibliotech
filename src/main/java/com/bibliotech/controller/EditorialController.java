package com.bibliotech.controller;

import com.bibliotech.entity.Editorial;
import com.bibliotech.service.EditorialService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/editoriales")
@CrossOrigin(origins = "*")
public class EditorialController {

    private final EditorialService editorialService;

    public EditorialController(EditorialService editorialService) {
        this.editorialService = editorialService;
    }

    @GetMapping
    public List<Editorial> findAll() {
        return editorialService.findAll();
    }

    @PutMapping("/{id}")
    public Editorial edit(@RequestBody Editorial editorial, @PathVariable Long id) {
        return editorialService.edit(editorial, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        editorialService.delete(id);
    }

}
