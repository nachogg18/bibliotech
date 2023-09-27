package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_categoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoCategoria extends Base {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fechaAlta")
    private Instant fechaAta = Instant.now();

    @Column(name = "fechaBaja")
    private Instant fechaBaja;
}
