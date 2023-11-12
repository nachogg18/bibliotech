package com.bibliotech.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "publicacion")
public class Publicacion extends Base {

    @Column
    private Integer anio;

    @Column(unique = true)
    private String isbn;

    @Column
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String sinopsis;

    @Column
    private Integer nroPaginas;

    @Column
    private Instant fechaAlta = Instant.now();

    @Column
    private Instant fechaBaja;

    @ManyToMany
    @JoinTable(name = "publicacion_autor",
            joinColumns = @JoinColumn(name = "publicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private List<Autor> autores;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CategoriaPublicacion> categoriaPublicacionList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "publicacion_editorial",
            joinColumns = @JoinColumn(name = "publicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "editorial_id"))
    private List<Editorial> editoriales = new ArrayList<>();

    @ManyToOne
    private Edicion edicion;

    @OneToMany
    @JoinTable(name = "publicacion_ejemplar",
            joinColumns = @JoinColumn(name = "publicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "ejemplar_id"))
    private List<Ejemplar> ejemplares = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "publicacion_comentario",
            joinColumns = @JoinColumn(name = "publicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "comentario_id"))
    private List<Comentario> comentarios = new ArrayList<>();

    @ManyToOne
    private TipoPublicacion tipoPublicacion;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private Link link;

}
