package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrestamoSearchDTO {
    private String dni;
    private String tituloPublicacion;
    private Instant fechaDesde;
    private Instant fechaHasta;
}
