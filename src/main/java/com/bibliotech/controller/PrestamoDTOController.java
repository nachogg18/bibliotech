package com.bibliotech.controller;

import com.bibliotech.dto.PrestamoDTO;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.security.service.UserService;
import com.bibliotech.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/crearPrestamo")
@Log4j2
@RequiredArgsConstructor
public class PrestamoDTOController {

    @Autowired
    private final PrestamoService prestamoService;

    @PostMapping
    public Prestamo save(@RequestBody PrestamoDTO prestamoDTO) throws Exception {
        Prestamo prestamo = prestamoService.convertDtoToEntity(prestamoDTO);
        System.out.println(prestamoDTO);
        return prestamoService.save(prestamo);
    }
}
