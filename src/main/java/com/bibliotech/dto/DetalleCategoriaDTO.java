package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;

import com.bibliotech.entity.CategoriaValor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetalleCategoriaDTO {
    private CategoriaDetalleDTO categoria;
    private List<CategoriaValor> valores = new ArrayList<>();
}
