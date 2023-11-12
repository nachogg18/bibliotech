package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PublicacionMobileSearchItem {
    private Long id;
    private String isbn;
    private String nombrePublicacion;
    private String sinopsisPublicacion;
    private String tipoPublicacion;
    private List<String> autores;
    private int anio;
    private List<String> editoriales;
    private String edicion;
}
