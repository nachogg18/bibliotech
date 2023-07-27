package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "publicacion_biblioteca")
public class PublicacionBiblioteca extends Base {
    private Date fechaDesde = new Date();
    private Date fechaHasta;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "biblioteca_id")
    private Biblioteca biblioteca;
}
