package com.bibliotech.security.dao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindUserDto {
    private Long id;
    private int dni;
    private int legajo;
    private String nombre;
    private String apellido;
}
