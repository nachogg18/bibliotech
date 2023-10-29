package com.bibliotech.dto;


import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PrestamoItemTablaDTO {
    private Long id;
    private String tituloPublicacion;
    private Long idEjemplar;
    private Long idUsuario;
    private Instant fechaDesde;
    private Instant fechaHasta;
    private String estado;
}
