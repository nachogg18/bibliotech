package com.bibliotech.utils.dto_reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInformePublicacion {
    private String publicacion;
    private int cantidad;
}
