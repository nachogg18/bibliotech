package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleMultaDTO {
    private Long idPrestamo;
    private String tituloPublicacion;
    private Long idEjemplar;
    private String descripcionTipoMulta;
    private Instant fechaInicioMulta;
    private Instant fechaFinMulta;
    private int cantidadDiasMulta;
    private String estadoMulta;
    private String descripcionMulta;
}
