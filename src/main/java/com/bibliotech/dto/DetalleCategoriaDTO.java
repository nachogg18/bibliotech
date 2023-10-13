package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;

import com.bibliotech.entity.CategoriaValor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetalleCategoriaDTO {
    private Long id;
    private String nombre;
    private List<CategoriaValor> valores = new ArrayList<>();
}
