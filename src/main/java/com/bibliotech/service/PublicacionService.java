package com.bibliotech.service;


import com.bibliotech.dto.*;
import com.bibliotech.entity.Comentario;
import com.bibliotech.entity.Publicacion;
import java.util.List;
import java.util.Optional;


public interface PublicacionService{
    List<PublicacionResponseDTO> findAll();
    Optional<Publicacion> findById(Long id);
    List<PublicacionResponseDTO> findAllPublicacionDTO(String parametro, String contenido, List<BusquedaPublicacionCategoriaDTO> busquedaPublicacionList);

    List<Publicacion> findByParams(FindPublicacionesByParamsDTO request);

    DetallePublicacionDTO getDetallePublicacion(Long id);

    PageDTO<PublicacionPaginadaDTO> findAllPublicacionPaginatedDTO(int page);


    PublicacionResponseDTO create(CreatePublicacionRequestDTO request);

    Publicacion save(Publicacion publicacion);

    ModificarPublicacionResponse updatePublicacion(ModificarPublicacionDTO req, Long id);

    List<ComentarioDTO> getAllComentarios(Long id);
}
