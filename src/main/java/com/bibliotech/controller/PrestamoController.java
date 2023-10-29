package com.bibliotech.controller;

import com.bibliotech.dto.*;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.service.PrestamoService;
import com.bibliotech.service.PrestamoServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PRESTAMO')")
    public List<PrestamoItemTablaDTO> getPrestamosListTable() {
        return prestamoService.getPrestamosListTable();
    }

    @PostMapping(path = "/findByParams")
        public List<PrestamosByParamsResponse> findByParams(
            @RequestBody PrestamosByParamsRequest request) {

        request.validate();

        return prestamoService.findByParams(request).stream()
                .map(
                        prestamo -> PrestamosByParamsResponse.prestamoToPrestamoByParamsResponse(prestamo))
                .collect(Collectors.toList());
    }
}
