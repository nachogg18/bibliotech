package com.bibliotech.controller;

import com.bibliotech.dto.PrestamoRequest;
import com.bibliotech.dto.PrestamoResponse;
import com.bibliotech.service.PrestamoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/prestamoService")
@Log4j2
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class PrestamoServiceController {

    @Autowired
    private final PrestamoService prestamoService;

    @PostMapping
    public ResponseEntity<PrestamoResponse> save(@RequestBody PrestamoRequest request) throws Exception {
        return ResponseEntity.ok().body(prestamoService.crearPrestamo(request));
    }


    @PatchMapping("/{id}/checkout")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('MODIFY', 'PRESTAMO')")
    public ResponseEntity<PrestamoResponse> checkOutPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.checkOutPrestamo(id));
    }

    @PatchMapping("/{id}/checkin")
    //@PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('MODIFY', 'PRESTAMO')")
    public ResponseEntity<PrestamoResponse> checkInPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok().body(prestamoService.checkInPrestamo(id));
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<PrestamoResponse> update(@RequestBody PrestamoRequest request) {
//        return ResponseEntity.ok().body(prestamoService.modifyPrestamo(request));
//    }
}
