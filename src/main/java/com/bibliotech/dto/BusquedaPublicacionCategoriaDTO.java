package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaPublicacionCategoriaDTO {
    private long idCategoria;
    private List<BusquedaPublicacionCategoriaValorDTO> valores = new ArrayList<>();
}
