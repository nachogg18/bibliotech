package com.bibliotech.controller;

import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.service.PrestamoService;
import com.bibliotech.service.PrestamoServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.bibliotech.dto.DetallePrestamoDTO;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/prestamos")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class PrestamoController extends BaseControllerImpl<Prestamo, PrestamoServiceImpl>{
    public final PrestamoService prestamoService;

    @GetMapping("/user/{idUsuario}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PRESTAMO')")
    public List<FindPrestamoDTO> getPrestamosByUserId(@PathVariable Long idUsuario) {
        return prestamoService.getPrestamosByUserId(idUsuario);
    }

    @GetMapping("/detalle/{id}")
    @PreAuthorize("@authenticacionService.hasPrivilegeOfDoActionForResource('READ', 'PRESTAMO')")
    public DetallePrestamoDTO getDetallePrestamo(@PathVariable Long id) {
        return prestamoService.getDetallePrestamo(id);
    }
}
