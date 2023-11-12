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
public class FindPrestamoDTO {
    private Long id;
    private String publicacion;
    private Long ejemplar;
    private Long publicacionId;
    private String estado;
    private Instant fechaInicio;
    private Instant fechaHasta;
}
