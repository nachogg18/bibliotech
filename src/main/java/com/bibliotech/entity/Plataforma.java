package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "plataforma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plataforma extends Base {
    @Column
    private String nombre;

    @OneToMany
    private List<Link> links = List.of();

    @Column
    private String instrucciones;
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
}
