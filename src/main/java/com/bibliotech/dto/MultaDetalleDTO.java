package com.bibliotech.dto;

import com.bibliotech.entity.MultaEstado;
import com.bibliotech.entity.TipoMulta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultaDetalleDTO {

    private String descripcion;
    private Instant fechaInicio;
    private Instant fechaFin;
    private Instant fechaAlta;
    private Instant fechaBaja;
    private TipoMulta tipoMulta;
    private List<MultaEstado> multaEstados;
    private Long idPrestamo;
    private String tituloPublicacionPrestamo;
    private Long idEjemplarPublicacionPrestamo;
    private String estadoPrestamo;
    private Instant fechaDesdePrestamo;
    private Instant fechaHastaPrestamo;
    private Long idUser;
    private String nombreUser;
    private String legajoUser;
    private String dniUser;
}
