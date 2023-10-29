package com.bibliotech.entity;

import com.bibliotech.security.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comentario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comentario extends Base {
    @Column
    private int calificacion;
    @Column
    private String comentario;
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
    @ManyToOne
    private User altaUsuario;
    @ManyToOne
    private User bajaUsuario;
}
