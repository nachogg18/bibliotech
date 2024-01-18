package com.bibliotech.controller;

import com.bibliotech.dto.*;
import com.bibliotech.entity.EstadoPrestamo;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.entity.PrestamoEstado;
import com.bibliotech.service.PrestamoService;
import com.bibliotech.service.PrestamoServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.ValidationException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PRESTAMO')")
    public DetallePrestamoDTO getDetallePrestamo(@PathVariable Long id) {
        return prestamoService.getDetallePrestamo(id);
    }

    @GetMapping("/list")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PRESTAMO')")
    public List<PrestamoItemTablaDTO> getPrestamosListTable() {
        return prestamoService.getPrestamosListTable();
    }

    @PostMapping(path = "/findByParams")
    public List<PrestamoDTO> findByParams(@RequestBody PrestamosByParamsRequest request) throws ValidationException {

        request.validate();

        return prestamoService.findByParams(request).stream()
                .map(
                        PrestamoDTO::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/search")
    public List<PrestamoSearchItemTablaDTO> searchPrestamos(@RequestBody PrestamoSearchDTO request) {
        return prestamoService.searchPrestamos(request);
    }

    @PostMapping("/crearPrestamo")
    public ResponseEntity<PrestamoResponse> save(@RequestBody PrestamoRequest request) throws Exception {
        return ResponseEntity.ok().body(prestamoService.crearPrestamo(request));
    }

    @PostMapping("/{id}/checkout")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<PrestamoResponse> checkOutPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.checkOutPrestamo(id));
    }

    @PostMapping("/{id}/checkin")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<PrestamoResponse> checkInPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.checkInPrestamo(id));
    }

    @PostMapping("/{id}/cancelar")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<PrestamoResponse> cancelarPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.cancelarPrestamo(id));
    }

    @PostMapping("/{id}/renovar")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<PrestamoResponse> renovarPrestamo(@PathVariable Long id, @RequestBody RenovacionDTO req) {
        return ResponseEntity.ok().body(prestamoService.renovarPrestamo(id, req));
    }

    @PostMapping("/{id}/extravio")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<PrestamoResponse> extravioPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.extravioPrestamo(id));
    }

    @GetMapping("/{id}/historial")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<List<PrestamoEstado>> historialEstadoPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.getHistorial(id));
    }

    @GetMapping("/{id}/renovaciones")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<List<Instant>> renovacionesPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.getRenovaciones(id));
    }

    @GetMapping("/a-multar")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'PRESTAMO')")
    public ResponseEntity<List<Prestamo>> prestamosVencidos() {
        return ResponseEntity.ok().body(prestamoService.getPrestamosAMultar());
    }

}
