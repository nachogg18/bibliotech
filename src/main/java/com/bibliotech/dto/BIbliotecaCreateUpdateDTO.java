package com.bibliotech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BIbliotecaCreateUpdateDTO {
    @NotNull
    private String ubicacion;
    @NotNull
    private String contacto;
    @NotNull
    private String nombre;
}
