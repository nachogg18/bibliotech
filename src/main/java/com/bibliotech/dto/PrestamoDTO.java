package com.bibliotech.dto;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class PrestamoDTO {
    private Instant fechaFinEstimada;
    private Instant fechaInicioEstimada;
    //private Date fechaBaja;
    private Long UsuarioID;
    private Long EjemplarID;
}
