package com.bibliotech.controller;

import com.bibliotech.dto.PrestamoDTO;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.entity.Usuario;
import com.bibliotech.service.PrestamoService;
import com.bibliotech.service.UsuarioService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/prestamosDTO")
@Log4j2
public class PrestamoDTOController {

    @Autowired
    private final PrestamoService prestamoService;

    private final UsuarioService usuarioService;

    public PrestamoDTOController (PrestamoService prestamoService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Prestamo save(@RequestBody PrestamoDTO prestamoDTO) throws Exception {
        Prestamo prestamo = prestamoService.convertDtoToEntity(prestamoDTO);
        System.out.println(prestamoDTO);
        return prestamoService.save(prestamo);
    }
}
