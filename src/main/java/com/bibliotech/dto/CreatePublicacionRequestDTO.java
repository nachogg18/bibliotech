package com.bibliotech.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

public record CreatePublicacionRequestDTO(
    @Positive
    Integer anioPublicacion,
    @NotEmpty
    String isbnPublicacion,
    @NotEmpty
    String tituloPublicacion,
    @Positive
    Integer nroPaginas,
    @NotEmpty
    List<String> idsAutores,
    @NotNull
    Long idEdicion,
    @NotNull
    Long idLink,
    @NotEmpty
    List<String> idsCategorias,
    @NotNull
    Long idTipo,

    @NotEmpty
    List<String> idsEditoriales
    )
{


    public List<Long> getValidatedIdsAutores() {
        return this.idsAutores.stream().map(
                idAutor -> Long.valueOf(idAutor)
        ).collect(Collectors.toList());
    }

    public List<Long> getValidatedIdsCategorias() {
        return this.idsCategorias.stream().map(
                idCategoria -> Long.valueOf(idCategoria)
        ).collect(Collectors.toList());
    }

    public List<Long> getValidatedIdsEditoriales() {
        return this.idsEditoriales.stream().map(
                idEditorial -> Long.valueOf(idEditorial)
        ).collect(Collectors.toList());
    }
}