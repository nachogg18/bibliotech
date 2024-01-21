package com.bibliotech.controller;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.service.PublicacionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
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

  @PostMapping(path = "/findByParams")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
  public List<PublicacionByParamsDTO> findByParams(
      @RequestBody FindPublicacionesByParamsDTO request) {

    request.validate();

    return publicacionService.findByParams(request).stream()
        .map(
            publicacion ->
                PublicacionByParamsDTO.builder()
                    .id(publicacion.getId())
                    .editoriales(
                        publicacion.getEditoriales().stream()
                            .map(
                                editorial ->
                                    Editorial.builder().nombre(editorial.getNombre()).build())
                            .collect(Collectors.toList()))
                    .anioPublicacion(publicacion.getAnio().intValue())
                    .autores(
                        publicacion.getAutores()
                                .stream()
                            .map(
                                autor ->
                                    Autor.builder()
                                        .nombre(autor.getNombre())
                                        .apellido(autor.getApellido())
                                        .nacionalidad(autor.getNacionalidad())
                                        .biografia(autor.getBiografia())
                                        .build())
                            .toList()
                    )
                    .edicion(Edicion.builder().nombre(publicacion.getEdicion().getNombre()).build())
                    .tituloPublicacion(publicacion.getTitulo())
                    .anioPublicacion(publicacion.getAnio())
                    .build())
        .collect(Collectors.toList());
  }

  @PostMapping
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'PUBLICACION')")
  public ResponseEntity<PublicacionResponseDTO> create(
      @RequestBody @Valid CreatePublicacionRequestDTO request) {

    return ResponseEntity.status(HttpStatus.OK).body(publicacionService.create(request));
  }

  @GetMapping
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
  public List<Publicacion> findAll() {
    return publicacionService.findAll();
  }

  @GetMapping("one/{id}")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
  public Optional<Publicacion> findOne(@PathVariable Long id) {
    return publicacionService.findById(id);
  }

//  @GetMapping(path = "/paged")
//  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
//  public ResponseEntity<PageDTO<PublicacionPaginadaDTO>> getAllPublicacionPaged(
//      @RequestParam(defaultValue = "1") int page) {
//    return ResponseEntity.status(HttpStatus.OK)
//        .body(publicacionService.findAllPublicacionPaginatedDTO(page));
//  }

  @GetMapping(path = "/{id}")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'PUBLICACION')")
  public ResponseEntity<DetallePublicacionDTO> getDetallePublicacion(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(publicacionService.getDetallePublicacion(id));
  }

  @PutMapping(path = "/{id}")
  public ResponseEntity<?> updatePublicacion(
      @RequestBody ModificarPublicacionDTO req, @PathVariable Long id) {
    return ResponseEntity.ok().body(publicacionService.updatePublicacion(req, id));
  }

  @GetMapping(path="/{id}/comentarios")
  //TODO cambiar la preautorizacion a lectura de comentarios
  public ResponseEntity<List<ComentarioDTO>> getComentarios(@PathVariable Long id){
    return ResponseEntity.ok().body(publicacionService.getAllComentarios(id));
  }

  @GetMapping(path = "/mobile/search")
  public ResponseEntity<List<PublicacionMobileSearchItem>> getPublicacionesMobile(@RequestParam(name = "input") String input){
    return ResponseEntity.ok().body(publicacionService.getPublicacionesMobile(input));
  }

  @GetMapping(path = "/{id}/link")
  public ResponseEntity<LinkMobileDto> getPublicacionLink(@PathVariable Long id){
    return ResponseEntity.ok().body(publicacionService.getPublicacionLink(id));
  }

  @GetMapping(path = "/titulos")
  public ResponseEntity<List<String>> getPublicacionTitulo(){
    return ResponseEntity.ok().body(publicacionService.getTitulos());
  }

  @GetMapping(path = "/anios")
  public ResponseEntity<List<String>> getPublicacionAnios(){
    return ResponseEntity.ok().body(publicacionService.getAnios());
  }

  @GetMapping(path = "/isbns")
  public ResponseEntity<List<String>> getPublicacionIsbns(){
    return ResponseEntity.ok().body(publicacionService.getIsbns());
  }

}
