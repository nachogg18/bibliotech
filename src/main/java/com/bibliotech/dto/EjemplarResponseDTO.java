package com.bibliotech.dto;

import com.bibliotech.entity.Ubicacion;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarResponseDTO {
    @NotNull
    private Long id;
    @NotNull
    private String serialNFC;
    @NotNull
    private float valoracion;
    @NotNull
    //List<ComentarioDTO> comentarios;
    private boolean tieneComentarios;
    @NotNull
    private String estado;
    @NotNull
    private Ubicacion ubicacion;


}
