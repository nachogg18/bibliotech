package com.bibliotech.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CategoriaDTO(
        @NotNull
        Long idCategoria,
        @NotEmpty
        List<Long> idValores
) {
    
}
