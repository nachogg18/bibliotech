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
@Table(name= "categoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria extends Base{

    @Column(name="nombre")
    private String nombre;

    @Column(name="fechaAlta")
    private Date fechaAlta = new Date();

    @Column(name="fechaBaja")
    private Date fechaBaja;
}
