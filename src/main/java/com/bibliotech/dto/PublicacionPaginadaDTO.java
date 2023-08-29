package com.bibliotech.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PublicacionPaginadaDTO {
    private String titulo;
    private List<String> autores;
    private List<String> editoriales;
    private String edicion;
    private int anio;
}
