package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModificarPublicacionResponse {
    private Long id;
    private String isbn;
    private String titulo;
}
