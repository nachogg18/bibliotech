package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;

@Entity
@Table(name = "tipo_multa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoMulta extends Base {
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
    @Column
    private String descripcion;
    @Column
    private int cantidadDias;
}
