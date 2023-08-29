package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comentario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comentario extends Base {
    @Column
    private float calificacion;
    @Column
    private String comentario;
    @Column
    private Date fechaAlta = new Date();
    @Column
    private Date fechaBaja;
}
