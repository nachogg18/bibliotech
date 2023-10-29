package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ejemplar_estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EjemplarEstado extends Base {
    @Column
    private Instant fechaInicio = Instant.now();
    @Column
    private Instant fechaFin;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private EstadoEjemplar estadoEjemplar;
}
