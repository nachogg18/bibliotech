package com.bibliotech.controller;

import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;
import com.bibliotech.entity.Multa;
import com.bibliotech.service.MultaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/multas")
@SecurityRequirement(name = "bearer-key")
@Log4j2
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;


    @GetMapping
    private List<Multa> findAll() {
        return multaService.findAll();
    }

    @PostMapping(path = "/findByParams")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'MULTA')")
    private List<MultaItemTablaDTO> findByParams(@RequestBody FindMultaByParamsDTO request) {
        request.validate();
        return multaService.findByParams(request);
    }

}
