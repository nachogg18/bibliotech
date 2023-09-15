package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroCategoriaDTO {
    private Long idCategoria;
    private String nombreCategoria;
    private List<FiltroCategoriaValorDTO> valores = new ArrayList<>();
}
