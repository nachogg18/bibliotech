package com.bibliotech.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.Instant;

@Data
@Builder
public class UbicacionResponseDTO {
    private Long id;
    private String descripcion;
    private boolean ocupada;
    private Instant fechaAlta;
    private Instant fechaBaja;
}
