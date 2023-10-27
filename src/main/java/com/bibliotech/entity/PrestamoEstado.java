package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name= "prestamo_estado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrestamoEstado extends Base{
    @Column
    private Instant fechaInicio = Instant.now();

    @Column
    private Instant fechaFin;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private EstadoPrestamo estado;

}
