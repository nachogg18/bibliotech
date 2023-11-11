package com.bibliotech.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "provincia")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Provincia extends Base {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fechaAlta")
    private Instant fechaAlta = Instant.now();

    @Column(name = "fechaBaja")
    private Instant fechaBaja;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;
}
