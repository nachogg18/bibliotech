package com.bibliotech.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "edicion")
public class Edicion extends Base {
    @NotNull
    private String nombre;
    @NotNull
    private Instant fechaAlta = Instant.now();
    private Instant fechaBaja;
}
