package com.bibliotech.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "autor")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Autor extends Base {

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "fechaAlta")
    private Instant fechaAlta = Instant.now();

    @Column(name = "fechaBaja")
    private Instant fechaBaja;

    @Column
    private Instant  fechaNacimiento;

    @Column
    private String nacionalidad;

    @Column(length = 1000)
    private String biografia;
}
