package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;

@Entity
@Table(name = "multa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Multa extends Base {
    @Column
    private String descripcion;
    @Column
    private Instant fechaInicio;
    @Column
    private Instant fechaFin;
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
    @ManyToOne
    private TipoMulta tipoMulta;
}
