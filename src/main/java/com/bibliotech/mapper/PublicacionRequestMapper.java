package com.bibliotech.mapper;

import com.bibliotech.dto.PublicacionResponseDTO;
import com.bibliotech.entity.Publicacion;

import java.util.List;

public class PublicacionRequestMapper {
    public static List<PublicacionResponseDTO> toResponseDTO(List<Publicacion> publicaciones) {
        return publicaciones.stream().map(
                publicacion -> {
                    PublicacionResponseDTO dto = new PublicacionResponseDTO();
                    dto.setId(publicacion.getId());
                    dto.setTituloPublicacion(publicacion.getTitulo());
                    publicacion.getAutores().forEach(a -> dto.getNombreAutores().add(a.getApellido().toUpperCase() + ", " + a.getNombre()));
                    dto.setNombreEditorial(publicacion.getEditoriales().get(0).getNombre()); // TODO: Tomar la ultima editorial
                    dto.setAnioPublicacion(publicacion.getAnio());
                    dto.setNombreEdicion(publicacion.getEdicion().getNombre());
                    return dto;
                }
        ).toList();
    }
}
