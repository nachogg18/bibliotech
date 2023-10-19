package com.bibliotech.controller;

import com.bibliotech.dto.PrestamoRequest;
import com.bibliotech.dto.PrestamoResponse;
import com.bibliotech.service.PrestamoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/crearPrestamo")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class PrestamoDTOController {

    @Autowired
    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoResponse> save(@RequestBody PrestamoRequest request) throws Exception {
        return ResponseEntity.ok().body(prestamoService.crearPrestamo(request));
    }
}
