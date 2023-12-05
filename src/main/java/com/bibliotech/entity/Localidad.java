package com.bibliotech.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "localidad")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Localidad extends Base {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigoPostal")
    private String codigoPostal;

    @Column(name = "fechaAlta")
    private Instant fechaAlta = Instant.now();

    @Column(name = "fechaBaja")
    private Instant fechaBaja;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;
    
}
