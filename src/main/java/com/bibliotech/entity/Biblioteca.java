package com.bibliotech.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    private String ubicacion;
    private String contacto;
    @NotNull
    private Instant fechaAlta = Instant.now();
    private Instant fechaBaja;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "biblioteca")
    private List<Ubicacion> ubicacionList = new ArrayList<>();
}
