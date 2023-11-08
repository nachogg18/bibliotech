package com.bibliotech.utils.dto_reporte;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInformePrestamo {
    private String titulo;
    private String edicion;
    private int anio;
    private int ejemplares;
    private int prestamos;
}
