package com.bibliotech.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicacionPaginadaDTO {
    private String titulo;
    private List<String> autores;
    private List<String> editoriales;
    private String edicion;
    private int anio;
}
