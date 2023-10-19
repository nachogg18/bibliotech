package com.bibliotech.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class AutorDTO {
    private Long id;
    private String nombre;
    private Instant fechaNacimiento;
    private String biografia;
    private String nacionalidad;
}
