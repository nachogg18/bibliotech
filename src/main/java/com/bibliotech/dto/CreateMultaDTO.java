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
public class CreateMultaDTO {
    private Long idUsuario;
    private Long idPrestamo;
    private Long idMotivoMulta;
    private Instant fechaInicioMulta;
}
