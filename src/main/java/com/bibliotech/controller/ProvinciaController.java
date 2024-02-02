package com.bibliotech.controller;

import com.bibliotech.dto.ProvinciaDTO;
import com.bibliotech.service.ProvinciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/provincias")
@RequiredArgsConstructor
public class ProvinciaController {

    private final ProvinciaService provinciaService;

    @GetMapping
    public ResponseEntity<List<ProvinciaDTO>> findAll() {
        return ResponseEntity.ok(provinciaService.findByFechaBajaNull().stream().map(
                ProvinciaDTO::toDto
        ).toList());
    }
}
