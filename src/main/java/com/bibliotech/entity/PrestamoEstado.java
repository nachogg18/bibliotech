package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name= "prestamo_estado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoEstado extends Base{
    @Column
    private Date fechaInicio = new Date();

    @Column
    private Date fechaFin;

    @ManyToOne
    private EstadoPrestamo estado;

}
