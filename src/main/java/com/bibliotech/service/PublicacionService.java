package com.bibliotech.service;


import com.bibliotech.dto.PublicacionResponseDTO;
import com.bibliotech.entity.Publicacion;

import java.util.List;

public interface PublicacionService extends BaseService<Publicacion, Long>{
    List<PublicacionResponseDTO> findAllPublicacionDTO();

}
