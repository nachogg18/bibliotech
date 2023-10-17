package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultaItemTablaDTO {
    private Long id;
    private Long idPrestamo;
    private Long idUsuario;
    private Instant fechaDesde;
    private Instant fechaHasta;
    private String estado;
    private String tipo;
}
