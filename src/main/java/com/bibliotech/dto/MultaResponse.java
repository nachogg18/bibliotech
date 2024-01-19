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
public class MultaResponse {
    private Long multaId;
    private Long prestamoId;
    private String publicacionTitulo;
    private String estadoMulta;
    private Instant fechaInicioMulta;
    private Instant fechaFinMulta;
    private String multaDescripcion;
}