package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;

@Entity
@Table(name = "multa_estado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultaEstado extends Base {
    @Column
    private String nombre;
    @Column
    private Instant fechaInicio = Instant.now();
    @Column
    private Instant fechaFin;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private EstadoMulta estadoMulta;
}
