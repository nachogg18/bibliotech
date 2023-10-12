package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TipoPublicacionDTO {
    private Long id;
    private String nombre;
}
