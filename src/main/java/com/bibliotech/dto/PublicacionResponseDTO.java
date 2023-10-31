package com.bibliotech.dto;

import com.bibliotech.entity.Publicacion;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicacionResponseDTO {
    private Long id;
    private String tituloPublicacion;
    private List<String> nombreAutores = new ArrayList<>();
    private String nombreEditorial;
    private int anioPublicacion;
    private String nombreEdicion;

    public static PublicacionResponseDTO PublicacionToPublicacionResponseDTO(Publicacion publicacion) {

        return PublicacionResponseDTO.builder()
                .id(Objects.nonNull(publicacion.getId()) ? publicacion.getId() : 0)
                .tituloPublicacion(Objects.nonNull(publicacion.getTitulo()) ? publicacion.getTitulo() : "")
                .nombreAutores(Objects.nonNull(publicacion.getAutores()) ? publicacion.getAutores().stream().map(autor -> autor.getApellido().toUpperCase() + "," + autor.getNombre()).collect(Collectors.toList()) : List.of())
                .nombreEditorial(Objects.nonNull(publicacion.getEditoriales()) && !publicacion.getEditoriales().isEmpty() ? publicacion.getEditoriales().get(0).getNombre(): "")
                .anioPublicacion(Objects.nonNull(publicacion.getAnio()) ? publicacion.getAnio() : 0)
                .nombreEdicion(Objects.nonNull(publicacion.getEdicion()) ? publicacion.getTitulo() : "")
                .build();
    }
}