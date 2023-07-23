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
@Table(name= "tipo_categoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoCategoria extends Base {

    @Column(name="nombre")
    private String nombre;

    @Column(name="fechaAlta")
    private Date fechaAta = new Date();

    @Column(name="fechaBaja")
    private Date fechaBaja;
}
