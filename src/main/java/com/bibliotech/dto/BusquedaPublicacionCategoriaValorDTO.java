package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaPublicacionCategoriaValorDTO {
    private long idValor;
    private String nombreValor;
    private boolean seleccionadoValor;

}
