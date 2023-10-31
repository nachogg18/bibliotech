package com.bibliotech.dto;

import com.bibliotech.entity.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarResponseDTO {
    private Long id;
    private String serialNFC;
    private float valoracion;
    private boolean tieneComentarios;
    private String estado;
    private Ubicacion ubicacion;
}
