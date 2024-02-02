package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearProvinciaDTO {
    private String nombre;
    private Long pais_id;
}
