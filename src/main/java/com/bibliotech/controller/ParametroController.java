package com.bibliotech.controller;

import com.bibliotech.entity.Parametro;
import com.bibliotech.service.ParametroService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/parametros")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class ParametroController {
    private final ParametroService parametroService;


    @GetMapping(path = "")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PARAMETRO')")
    public List<Parametro> obtenerParametros() {
        return parametroService.obtenerParametros();
    }

    @GetMapping(path = "/{nombre}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PARAMETRO')")
    public Optional<Parametro> obtenerParametro(@PathVariable String nombre) {
        return parametroService.obtenerParametroPorNombre(nombre);
    }

    @PostMapping(path = "")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('WRITE', 'PARAMETRO')")
    public Parametro actualizarParametro(@RequestBody Parametro request) {
        Optional<Parametro> parametro = parametroService.obtenerParametroPorNombre(request.getNombre());
        if (parametro.isPresent()) {
            parametro.get().setValor(request.getValor());
            Parametro parametroActualizado = parametroService.actualizarParametro(request.getNombre(), request.getValor());
            return parametroActualizado;
        }

        return parametroService.guardarParametro(request);
    }
}
