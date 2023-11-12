package com.bibliotech.dto;

import com.bibliotech.entity.EstadoPrestamo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
public class PrestamoSearchItemTablaDTO {
    private final Long id;
    private final String tituloPublicacion;
    private final Long idEjemplar;
    private final Long idUsuario;
    private final Instant fechaDesde;
    private final Instant fechaHasta;
    private final String estado;

    public PrestamoSearchItemTablaDTO(Long id, String titulo, Long idEjemplar, Long idUsuario, Instant fechaDesde, Instant fechaHasta, EstadoPrestamo estado) {
        this.id = id;
        this.tituloPublicacion = titulo;
        this.idEjemplar = idEjemplar;
        this.idUsuario = idUsuario;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.estado = estado.name().toString();
    }
}
