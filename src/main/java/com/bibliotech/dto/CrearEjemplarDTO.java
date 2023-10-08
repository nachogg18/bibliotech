package com.bibliotech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearEjemplarDTO {
    @NotNull
    private Long idPublicacion;
    @NotNull
    private String idInventario;
    @NotNull
    private Long idUbicacion;
}
