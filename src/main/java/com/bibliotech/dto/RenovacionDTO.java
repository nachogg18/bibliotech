package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RenovacionDTO {
    private Instant fechaInicioRenovacion;
    private Instant fechaFinRenovacion;
}
