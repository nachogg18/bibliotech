package com.bibliotech.dto;

import lombok.*;

@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BibliotecaCreateDTO {
    private String nombre;
    private String ubicacion;
    private String contacto;
}
