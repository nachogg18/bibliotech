package com.bibliotech.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PrestamoDTO {
    private Date fechaFinEstimada;
    private Date fechaInicioEstimada;
    //private Date fechaBaja;
    private Long UsuarioID;
    private Long EjemplarID;
}
