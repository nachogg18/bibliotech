package com.bibliotech.controller;

import com.bibliotech.dto.CreateMultaDTO;
import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;
import com.bibliotech.dto.MultaResponse;
import com.bibliotech.service.MultaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/multas")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;

    @PostMapping(path = "/findByParams")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'MULTA')")
    public List<MultaItemTablaDTO> findByParams(@RequestBody FindMultaByParamsDTO request) {
        request.validate();
        return multaService.findByParams(request);
    }

    @PostMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'MULTA')")
    public boolean createMulta(@RequestBody CreateMultaDTO request) throws Exception {
        return multaService.createMultaPrestamo(request);
    }

    @PostMapping("{id}/finalizar")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'MULTA')")
    public ResponseEntity<MultaResponse> finalizarMulta(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(multaService.finalizarMulta(id));
    }

    @PostMapping("{id}/cancelar")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'MULTA')")
    public ResponseEntity<MultaResponse> cancelarMulta(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok().body(multaService.cancelarMulta(id));
    }

}
