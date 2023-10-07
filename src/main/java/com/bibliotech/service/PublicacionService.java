package com.bibliotech.service;


import com.bibliotech.dto.*;
import com.bibliotech.entity.Publicacion;
import java.util.List;
import java.util.Optional;

public interface PublicacionService {
    List<Publicacion> findAll();
    Optional<Publicacion> findById(Long id);
    List<PublicacionResponseDTO> findAllPublicacionDTO(String parametro, String contenido, List<BusquedaPublicacionCategoriaDTO> busquedaPublicacionList);

    List<Publicacion> findByParams(FindPublicacionesByParamsDTO request);

    DetallePublicacionDTO getDetallePublicacion(Long id);

    PageDTO<PublicacionPaginadaDTO> findAllPublicacionPaginatedDTO(int page);


    Publicacion create(CreatePublicacionRequestDTO request);

    Publicacion save(Publicacion publicacion);
}
