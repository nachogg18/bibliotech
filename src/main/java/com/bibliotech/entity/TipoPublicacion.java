package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
}
