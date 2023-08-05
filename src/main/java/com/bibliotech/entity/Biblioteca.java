package com.bibliotech.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "biblioteca")
public class Biblioteca extends Base {
    @NotNull
    private String nombre;
    @NotNull
    private Date fechaAlta = new Date();
    private Date fechaBaja;
}
