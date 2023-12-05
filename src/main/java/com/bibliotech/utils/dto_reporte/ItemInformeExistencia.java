package com.bibliotech.utils.dto_reporte;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemInformeExistencia {
    private String nfcSerial;
    private int idEjemplar;
    private String publicacion;
    private String estado;
}
