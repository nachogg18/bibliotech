package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria extends Base {

    @Column
    private String nombre;

    @Column
    private Instant fechaAlta = Instant.now();

    @Column
    private Instant fechaBaja;

    @OneToMany(fetch = FetchType.EAGER)
    private List<CategoriaValor> categoriaValorList = new ArrayList<>();
}
