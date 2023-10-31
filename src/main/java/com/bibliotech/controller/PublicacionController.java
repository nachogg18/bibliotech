package com.bibliotech.controller;

import com.bibliotech.dto.*;
import com.bibliotech.service.PublicacionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/publicaciones")
@Log4j2
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class PublicacionController {
  private final PublicacionService publicacionService;

  @PostMapping("/findByParams")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
  public ResponseEntity<List<PublicacionResponseDTO>> findByParams(
      @RequestBody FindPublicacionesByParamsDTO request) {

    request.validate();

    return ResponseEntity.ok().body(publicacionService.findByParams(request).stream()
        .map(
            publicacion ->
                PublicacionResponseDTO.builder()
                    .id(publicacion.getId())
                    .tituloPublicacion(publicacion.getTitulo())
                    .nombreAutores(
                        (Objects.nonNull(publicacion.getAutores()))
                            ? publicacion.getAutores().stream()
                                .map(
                                    autor ->
                                        String.format(
                                            "%s %s", autor.getNombre(), autor.getApellido()))
                                .toList()
                            : List.of())
                    .nombreEditorial(
                        (Objects.nonNull(publicacion.getEditoriales()) && !publicacion.getEditoriales().isEmpty())
                            ? publicacion.getEditoriales().get(0).getNombre()
                            : "")
                    .anioPublicacion(
                        (Objects.nonNull(publicacion.getAnio())) ? publicacion.getAnio() : 0)
                    .build())
        .collect(Collectors.toList()));
    }

  @PostMapping
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'PUBLICACION')")
  public ResponseEntity<PublicacionResponseDTO> create(
      @RequestBody @Valid CreatePublicacionRequestDTO request) {

    return ResponseEntity.status(HttpStatus.OK).body(publicacionService.create(request));
  }

  @GetMapping
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
  public List<PublicacionResponseDTO> findAll() {
    return publicacionService.findAll();
  }

  @GetMapping(path = "/paged")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
  public ResponseEntity<PageDTO<PublicacionPaginadaDTO>> getAllPublicacionPaged(
      @RequestParam(defaultValue = "1") int page) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(publicacionService.findAllPublicacionPaginatedDTO(page));
  }

  @GetMapping(path = "/{id}")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
  public ResponseEntity<DetallePublicacionDTO> getDetallePublicacion(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(publicacionService.getDetallePublicacion(id));
  }

  @PatchMapping(path = "/{id}")
  public ResponseEntity<?> updatePublicacion(
      @RequestBody ModificarPublicacionDTO req, @PathVariable Long id) {
    return ResponseEntity.ok().body(publicacionService.updatePublicacion(req, id));
  }
}
