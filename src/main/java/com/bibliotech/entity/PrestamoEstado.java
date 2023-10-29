package com.bibliotech.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Entity
@Builder
@Table(name= "prestamo_estado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoEstado extends Base{
    @Column
    private Instant fechaInicio = Instant.now();

    @Column
    private Instant fechaFin;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private EstadoPrestamo estado;

}
