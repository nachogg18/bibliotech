package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ejemplar_estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarEstado extends Base {
    @Column
    private Date fechaInicio = new Date();
    @Column
    private Date fechaFin;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private EstadoEjemplar estadoEjemplar;
}
