package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categoria_publicacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaPublicacion extends Base {
    @ManyToOne
    private Categoria categoria;
    @ManyToMany
    private List<CategoriaValor> categoriaValorList = new ArrayList<>();
}
