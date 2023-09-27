package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;

@Entity
@Table(name = "evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Evento extends Base {
    @Column(name = "fechaAlta")
    private Instant fechaAlta = Instant.now();
    @Column(name = "fechaBaja")
    private Instant fechaBaja;
    @ManyToOne
    private Ejemplar ejemplar;
    @Column(name = "tipoEvento")
    private TipoEvento tipoEvento;
}
