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
@Table(name = "tipo_publicacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoPublicacion extends Base {
    @Column
    private String nombre;
    @Column
    private Date fechaAlta = new Date();
    @Column
    private Date fechaBaja;
}
