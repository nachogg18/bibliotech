package com.bibliotech.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModificarPublicacionDTO {
    @Positive
    private Integer anioPublicacion;
    @NotEmpty
    private String isbnPublicacion;
    @NotEmpty
    private String tituloPublicacion;
    @Positive
    private Integer nroPaginas;
    @NotEmpty
    private List<Long> idsAutores = new ArrayList<>();
    @NotNull
    private Long idEdicion;
    private LinkRequestDTO link;
    @NotEmpty
    private List<CategoriaDTO> categorias = new ArrayList<>();
    @NotNull
    private Long idTipoPublicacion;
    @NotEmpty
    List<Long> idsEditoriales = new ArrayList<>();

//    private Integer anio;
//    private String isbn;
//    private String titulo;
//    private Integer nroPaginas;
//    private Long idEdicion;
//    private LinkDTO link;
//    private List<Long> idsCategorias;
//    private List<Long> idsAutores;
//    private List<Long> idsEditoriales;
//    private Long idTipoPublicacion;
}
