package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "publicacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publicacion extends Base {

    @Column
    private short anio;

    @Column
    private String isnb;

    @Column
    private String titulo;

    @Column
    private Date fechaAlta = new Date();

    @Column
    private Date fechaBaja;

    @Column
    private String descripcion;

    @ManyToMany
    @JoinTable(name = "publicacion_autor",
            joinColumns = @JoinColumn(name = "publicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private List<Autor> autores;

//    @ManyToMany
//    @JoinTable(name = "publicacion_categoria",
//            joinColumns = @JoinColumn(name = "publicacion_id"),
//            inverseJoinColumns = @JoinColumn(name = "categoria_id"))
//    private List<Categoria> categorias;

    @ManyToMany
    @JoinTable(name = "publicacion_editorial",
            joinColumns = @JoinColumn(name = "publicacion_id"),
            inverseJoinColumns = @JoinColumn(name = "editorial_id"))
    private List<Editorial> editoriales;

    @ManyToOne
    private Edicion edicion;

//    @Column
//    private TipoPublicacion tipoPublicacion;

}