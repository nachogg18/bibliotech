package com.bibliotech.service;


import com.bibliotech.dto.*;

import java.util.List;

public interface PublicacionService {
    List<PublicacionResponseDTO> findAllPublicacionDTO(String parametro, String contenido, List<BusquedaPublicacionCategoriaDTO> busquedaPublicacionList);
    DetallePublicacionDTO getDetallePublicacion(Long id);
    PageDTO<PublicacionPaginadaDTO> findAllPublicacionPaginatedDTO(int page);
}
