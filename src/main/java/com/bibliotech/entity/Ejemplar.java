package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity
@Table(name = "ejemplar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ejemplar extends Base {
    @Column
    private String serialNFC;
    @Column(name = "fechaAlta")
    private Instant fechaAlta = Instant.now();
    @Column(name = "fechaBaja")
    private Instant fechaBaja;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<EjemplarEstado> ejemplarEstadoList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "ejemplar_comentario",
            joinColumns = @JoinColumn(name = "publicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "comentario_id"))
    private List<Comentario> comentarios = new ArrayList<>();

    @OneToMany
    private List<Evento> eventos = new ArrayList<>();

    @ManyToOne
    private Publicacion publicacion;

    @ManyToOne
    private Ubicacion ubicacion;

}
