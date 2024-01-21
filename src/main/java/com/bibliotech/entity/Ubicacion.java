package com.bibliotech.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ubicacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion extends Base {
    @Column(unique = true)
    private String descripcion;
    @Column
    private boolean ocupada;
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
    @JsonIgnoreProperties("ubicacionList")
    @ManyToOne
    private Biblioteca biblioteca;
}
