package com.bibliotech.utils.dto_reporte;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInformeTiempo {
    private Integer dias;
    private Integer cantidad;
    private Integer total_prestamos;
}
