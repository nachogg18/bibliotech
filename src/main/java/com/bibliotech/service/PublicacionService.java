package com.bibliotech.service;


import com.bibliotech.dto.*;
import com.bibliotech.entity.Publicacion;

import java.util.List;

public interface PublicacionService {
    List<Publicacion> findAll();
    List<PublicacionResponseDTO> findAllPublicacionDTO(String parametro, String contenido, List<BusquedaPublicacionCategoriaDTO> busquedaPublicacionList);

    DetallePublicacionDTO getDetallePublicacion(Long id);

    PageDTO<PublicacionPaginadaDTO> findAllPublicacionPaginatedDTO(int page);
}
