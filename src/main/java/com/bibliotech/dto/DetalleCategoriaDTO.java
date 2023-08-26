package com.bibliotech.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DetalleCategoriaDTO {
    private String nombre;
    private List<String> valores = new ArrayList<>();
}
