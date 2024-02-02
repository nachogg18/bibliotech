package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearLocalidadDTO {
    private String nombre;
    private Long provincia_id;
    private String codigo_postal;
}
