package com.bibliotech.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categoria_publicacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaPublicacion extends Base {
    @ManyToOne
    private Categoria categoria;
    @ManyToMany
    private List<CategoriaValor> categoriaValorList = new ArrayList<>();
}
