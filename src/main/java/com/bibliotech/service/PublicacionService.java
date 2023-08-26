package com.bibliotech.service;


import com.bibliotech.dto.BusquedaPublicacionCategoriaDTO;
import com.bibliotech.dto.DetallePublicacionDTO;
import com.bibliotech.dto.PublicacionResponseDTO;

import java.util.List;

public interface PublicacionService {
    List<PublicacionResponseDTO> findAllPublicacionDTO(String parametro, String contenido, List<BusquedaPublicacionCategoriaDTO> busquedaPublicacionList);
    DetallePublicacionDTO getDetallePublicacion(Long id);
}
