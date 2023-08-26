package com.bibliotech.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetalleEjemplarDTO {
    private long id;
    private boolean disponibilidad;
    private double valoracion;
}
