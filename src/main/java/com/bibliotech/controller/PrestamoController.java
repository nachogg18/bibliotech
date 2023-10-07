package com.bibliotech.controller;

import com.bibliotech.entity.Prestamo;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.service.PrestamoServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/prestamos")
@SecurityRequirement(name = "bearer-key")
@Log4j2
public class PrestamoController extends BaseControllerImpl<Prestamo, PrestamoServiceImpl>{
    @Autowired
    public PrestamosRepository prestamosRepository;
}
