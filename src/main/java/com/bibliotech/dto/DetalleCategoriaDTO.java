package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetalleCategoriaDTO {
    private String nombre;
    private List<String> valores = new ArrayList<>();
}
