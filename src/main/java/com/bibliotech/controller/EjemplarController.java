package com.bibliotech.controller;

import com.bibliotech.dto.*;

import com.bibliotech.entity.Ejemplar;
import com.bibliotech.service.EjemplarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/ejemplares")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class EjemplarController {

    private final EjemplarService ejemplarService;

    @PostMapping(path = "")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'EJEMPLAR')")
    public Ejemplar create(@RequestBody @Valid CrearEjemplarDTO request) throws Exception {
        return ejemplarService.createEjemplar(request);
    }

    @GetMapping
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'EJEMPLAR')")
    public List<EjemplarResponseDTO> findAll() {
        return ejemplarService.findAll();
    }

    @GetMapping("/publicaciones/{publicacionId}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'EJEMPLAR')")
    public List<EjemplarResponseDTO> findEjemplaresByPublicacionId(@PathVariable Long publicacionId) {
        return ejemplarService.findEjemplaresByPublicacionId(publicacionId);
    }
    @GetMapping("/serial/{serialNFC}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'EJEMPLAR')")
    public EjemplarNFCDTO findEjemplarByNFC(@PathVariable String serialNFC) {
        return ejemplarService.busquedaEjemplarNFC(serialNFC);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'EJEMPLAR')")
    public ResponseEntity<EjemplarDetailDTO> findOne(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ejemplarService.findOne(id));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'EJEMPLAR')")
    public Ejemplar edit(@RequestBody EditEjemplarDTO request, @PathVariable Long id) {
        return ejemplarService.edit(request, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'EJEMPLAR')")
    public void delete(@PathVariable Long id) {
        ejemplarService.delete(id);
    }

    @GetMapping(path="{id}/comentarios")
    //TODO cambiar la preautorizacion a lectura de comentarios
    public ResponseEntity<List<ComentarioDTO>> getComentarios(@PathVariable Long id){
        return ResponseEntity.ok().body(ejemplarService.getAllComentarios(id));
    }

    @PutMapping("/{id}/reparar")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'EJEMPLAR')")
    public ResponseEntity<EditEjemplarDTO> repararEjemplar(@PathVariable Long id) {
        return ResponseEntity.ok().body(ejemplarService.repararEjemplar(id));
    }

    @PutMapping("/{id}/extraviar")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'EJEMPLAR')")
    public ResponseEntity<EditEjemplarDTO> extraviarEjemplar(@PathVariable Long id) {
        return ResponseEntity.ok().body(ejemplarService.extraviarEjemplar(id));
    }

    @PutMapping("/{id}/habilitar")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'EJEMPLAR')")
    public ResponseEntity<EditEjemplarDTO> habilitarEjemplar(@PathVariable Long id) {
        return ResponseEntity.ok().body(ejemplarService.habilitarEjemplar(id));
    }

}
