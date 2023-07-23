package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name= "ejemplar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ejemplar extends Base{

    @Column(name="valoracionPromedio")
    private float valoracionPromedio;

    @Column(name="nroInventario")
    private int nroInventario;

    @Column(name="fechaAlta")
    private Date fechaAlta;

    @Column(name="fechaBaja")
    private Date fechaBaja;
}
