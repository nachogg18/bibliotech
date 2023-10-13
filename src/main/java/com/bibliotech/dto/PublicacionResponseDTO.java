package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionResponseDTO {
    private Long id;
    private String titulo;
    private List<String> autores = new ArrayList<>();
    private List<String> editoriales = new ArrayList<>();
    private String tipoPublicacion;
    private int anio;
    private String edicion;
}
