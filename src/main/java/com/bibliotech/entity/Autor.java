package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name= "autor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Autor extends Base {

    @Column(name="nombre")
    private String nombre;

    @Column(name="apellido")
    private String apellido;

    @Column(name="fechaAlta")
    private Date fechaAlta = new Date();

    @Column(name="fechaBaja")
    private Date fechaBaja;

    @Column
    private Date fechaNacimiento;

    @Column
    private String nacionalidad;

    @Column
    private String biografia;
}
