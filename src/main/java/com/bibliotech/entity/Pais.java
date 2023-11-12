package com.bibliotech.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "pais")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pais extends Base {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "siglas")
    private String siglas;

    @Column(name = "fechaAlta")
    private Instant fechaAlta = Instant.now();

    @Column(name = "fechaBaja")
    private Instant fechaBaja;


}
