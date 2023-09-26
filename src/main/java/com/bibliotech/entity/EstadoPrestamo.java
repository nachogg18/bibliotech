package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name= "estado_prestamo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoPrestamo extends Base{
    @Column
    private String nombreEstado;

    @Column
    private Date fechaAltaEstadoPrestamo = new Date();

    @Column
    private Date fechaBajaEstadoPrestamo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "prestamo_estado")
    private List<PrestamoEstado> estados;

}
