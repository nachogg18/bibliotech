package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EjemplarNFCDTO {
    private Long idPublicacion;
    private Long idEjemplar;
    private String biblioteca;
    private String ubicacion;
    private float valoracion;
    private String isbn;
    private String nombrePublicacion;
    private String sinopsisPublicacion;
    private String tipoPublicacion;
    private int anio;
    private String edicion;
    List<String> autores;
    List<String> editoriales;
}
