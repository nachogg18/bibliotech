package com.bibliotech.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePublicacionRequestDTO {
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
    private List<CategoriaDTO> categorias = new ArrayList<>();
    @NotNull
    private Long idTipoPublicacion;
    @NotEmpty
    List<Long> idsEditoriales = new ArrayList<>();
}
