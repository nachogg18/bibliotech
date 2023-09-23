package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "carrera")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrera extends Base {
    @Column
    private String nombre;
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
}
