package com.bibliotech.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioDTO {
    private Long id;
    private String publicacion;
    private Long ejemplar;
    private int calificacion;
    private String comentario;
    private Instant fecha;
    private String altaUsuario;
//    private String bajaUsuario;
}
