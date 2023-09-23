package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ejemplar_estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarEstado extends Base {
    @Column
    private Instant fechaInicio = Instant.now();
    @Column
    private Instant fechaFin;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private EstadoEjemplar estadoEjemplar;
}
