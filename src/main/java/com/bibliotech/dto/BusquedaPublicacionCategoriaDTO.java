package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaPublicacionCategoriaDTO {
    private long idCategoria;
    private List<BusquedaPublicacionCategoriaValorDTO> valores = new ArrayList<>();
}
