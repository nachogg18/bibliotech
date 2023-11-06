package com.bibliotech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioDTO {
    @NotNull
    private Long id;
    @NotNull
    private int calificacion;
    @NotNull
    private String comentario;
    @NotNull
    private String fecha;
    @NotNull
    private String altaUsuario;
//    private String bajaUsuario;
}