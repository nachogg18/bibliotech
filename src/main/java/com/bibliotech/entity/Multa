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
    @Column
    private Instant fecha = Instant.now();
    @Column
    private String comentario;
}
