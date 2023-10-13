package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
@Data
@Builder
public class PrestamoDTO {
    private Long idUsuario;
    private Long idEjemplar;
    private Instant fechaFinEstimada;
    private Instant fechaInicioEstimada;
}
