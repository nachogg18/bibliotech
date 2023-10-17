package com.bibliotech.controller;

import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.service.PrestamoService;
import com.bibliotech.service.PrestamoServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/prestamos")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class PrestamoController extends BaseControllerImpl<Prestamo, PrestamoServiceImpl>{
    public final PrestamoService prestamoService;

    public List<FindPrestamoDTO> getPrestamos() {
        return prestamoService.getPrestamos();
    }
}
