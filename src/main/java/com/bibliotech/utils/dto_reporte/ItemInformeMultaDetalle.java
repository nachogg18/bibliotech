package com.bibliotech.utils.dto_reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInformeMultaDetalle {
    private int id;
    private String fecha_inicio;
    private String fecha_fin;
    private int legajo;
    private String tipo;
}
