package com.bibliotech.mapper;

import com.bibliotech.dto.PublicacionResponseDTO;
import com.bibliotech.entity.Publicacion;
import com.bibliotech.entity.Editorial;

import java.util.List;

public class PublicacionRequestMapper {
    public static List<PublicacionResponseDTO> toResponseDTO(List<Publicacion> publicaciones) {
        return publicaciones.stream().map(
                publicacion -> {
                    PublicacionResponseDTO dto = new PublicacionResponseDTO();
                    dto.setId(publicacion.getId());
                    dto.setTitulo(publicacion.getTitulo());
                    dto.setAutores(publicacion.getAutores().stream().map(a -> a.getApellido().toUpperCase() + ", " + a.getNombre()).toList());
                    dto.setEditoriales(publicacion.getEditoriales().stream().map(Editorial::getNombre).toList());
                    dto.setAnio(publicacion.getAnio());
                    dto.setEdicion(publicacion.getEdicion().getNombre());
                    return dto;
                }
        ).toList();
    }
}
