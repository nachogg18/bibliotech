package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class DetallePrestamoDTO {
    private String nombreApellidoUsuario;
    private Long idUsuario;
    private int dniUsuario;
    private String tituloPublicacion;
    private Long idEjemplar;
    private Instant fechaInicioPrestamo;
    private Instant fechaFinPrestamo;
    private Instant fechaDevolucion;
    private String estado;
    private List<RenovacionDTO> renovaciones;
}
