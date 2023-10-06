package com.bibliotech.entity;

import jakarta.persistence.*;

import java.time.Instant;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categoria_valor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaValor extends Base {
    @Column
    private String nombre;
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
    @ManyToOne
    private Categoria categoria;
}
