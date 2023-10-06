package com.bibliotech.controller;

import com.bibliotech.dto.DetalleMultaDTO;
import com.bibliotech.dto.MultaDTO;
import com.bibliotech.entity.Multa;
import com.bibliotech.repository.MultaRepository;
import com.bibliotech.security.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/v1")
public class MultaController {
    //    private final MultaService multaService;
    private final MultaRepository multaRepository;

    private MultaController(
//      MultaService multaService
            MultaRepository multaRepository) {
//        this.multaService = multaService;
        this.multaRepository = multaRepository;
    }

    @GetMapping(path = "/user/{id}/multas")
    private ResponseEntity<List<MultaDTO>> getMultas(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if (!Objects.equals(user.getId(), id))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(
                multaRepository.findAllById(List.of(id)).stream().map(
                        multa -> {
                            MultaDTO multaDTO = new MultaDTO();
                            multaDTO.setMultaId(multa.getId());
                            multaDTO.setPrestamoId(multa.getPrestamo().getId());
                            multaDTO.setPublicacionTitulo(multa.getPrestamo().getEjemplar().getPublicacion().getTitulo());
                            multaDTO.setMultaDescripcion(multa.getTipoMulta().getDescripcion());
                            multaDTO.setFechaInicioMulta(multa.getFechaInicio());
                            multaDTO.setFechaFinMulta(multa.getFechaFin());
                            multaDTO.setEstadoMulta(multa.getMultaEstados().stream().filter(me -> me.getFechaFin() == null).toList().get(0).getEstadoMulta().name());
                            return multaDTO;
                        }
                ).toList());
    }

    /* DETALLE MULTA */
    // TODO: lanzar excepcion si no existe userId o id, verificar que user.getId() = id funcione correctamente
    @GetMapping("/user/{userId}/multas/{id}")
    private ResponseEntity<Object> detalleMulta(@PathVariable Long userId, @PathVariable Long id, @AuthenticationPrincipal User user) {
        if (!Objects.equals(user.getId(), id))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        Multa multa = multaRepository.findByIdAndUserId(id, userId).get();

        DetalleMultaDTO detalleMultaDTO = new DetalleMultaDTO();
        detalleMultaDTO.setIdPrestamo(multa.getPrestamo().getId());
        detalleMultaDTO.setTituloPublicacion(multa.getPrestamo().getEjemplar().getPublicacion().getTitulo());
        detalleMultaDTO.setIdEjemplar(multa.getPrestamo().getEjemplar().getId());
        detalleMultaDTO.setDescripcionTipoMulta(multa.getTipoMulta().getDescripcion());
        detalleMultaDTO.setFechaInicioMulta(multa.getFechaInicio());
        detalleMultaDTO.setFechaFinMulta(multa.getFechaFin());
        detalleMultaDTO.setCantidadDiasMulta(multa.getTipoMulta().getCantidadDias());
        detalleMultaDTO.setEstadoMulta(multa.getMultaEstados().stream().filter(me -> me.getFechaFin() == null).toList().get(0).getEstadoMulta().name());
        detalleMultaDTO.setDescripcionMulta(multa.getDescripcion());
        return ResponseEntity.status(HttpStatus.OK)
                .body(detalleMultaDTO);
    }

}
