package com.bibliotech.controller;

import com.bibliotech.dto.LocalidadDTO;
import com.bibliotech.service.LocalidadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/localidades")
@RequiredArgsConstructor
public class LocalidadController {

    private final LocalidadService localidadService;

    @GetMapping
    public ResponseEntity<List<LocalidadDTO>> findAll() {
        return ResponseEntity.ok(localidadService.findByFechaBajaNull().stream().map(
                LocalidadDTO::toDto
        ).toList());
    }

}
