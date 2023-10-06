package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostrarCategoriaDTO {
    private String nombre;
    private List<MostrarCategoriaValorDTO> valores = new ArrayList<>();
}
