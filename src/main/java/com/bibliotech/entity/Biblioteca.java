package com.bibliotech.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "biblioteca")
public class Biblioteca extends Base {
    @NotNull
    private String nombre;
    @NotNull
    private Instant fechaAlta = Instant.now();
    private Instant fechaBaja;
}
