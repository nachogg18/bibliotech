package com.bibliotech.utils.dto_reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInformeRenovacion {
    private Integer prestamos_renovados;
    private Integer prestanos_no_renovados;
}
