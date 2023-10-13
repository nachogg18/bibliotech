package com.bibliotech.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModificarPublicacionDTO {
    private Integer anio;
    private String isbn;
    private String titulo;
    private Integer nroPaginas;
    private Long idEdicion;
    private LinkDTO link;
    private List<Long> idsCategorias;
    private List<Long> idsAutores;
    private List<Long> idsEditoriales;
    private Long idTipoPublicacion;
}
