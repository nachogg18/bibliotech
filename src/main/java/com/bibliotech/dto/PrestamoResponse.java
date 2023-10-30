package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PrestamoResponse {
    private Long PrestamoID;
    private Instant fechaFinEstimada;
    private Instant fechaInicioEstimada;
    private Long usuarioID;
    private Long ejemplarID;
    private Instant fechaAlta;

    private String estado;
}
