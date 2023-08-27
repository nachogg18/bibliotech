package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ejemplar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ejemplar extends Base {
    @Column
    private Long serialNFC;

    @Column(name = "fechaAlta")
    private Date fechaAlta = new Date();

    @Column(name = "fechaBaja")
    private Date fechaBaja;

    @OneToMany
    private List<EjemplarEstado> ejemplarEstadoList = new ArrayList<>();

    @OneToMany
    private List<Comentario> comentarios = new ArrayList<>();
}
