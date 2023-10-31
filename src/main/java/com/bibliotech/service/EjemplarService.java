package com.bibliotech.service;


import com.bibliotech.dto.*;
import com.bibliotech.entity.Ejemplar;

import java.util.List;
import java.util.Optional;

public interface EjemplarService {
    List<EjemplarResponseDTO> findAll();

    EjemplarDetailDTO findOne(Long id);

    List<EjemplarResponseDTO> findEjemplaresByPublicacionId(Long publicacionId);

    Ejemplar createEjemplar(CrearEjemplarDTO request) throws Exception;

    Ejemplar save(Ejemplar ejemplar);

    Ejemplar edit(EditEjemplarDTO ejemplar, Long id);

    void delete(Long id);

    Optional<Ejemplar> findById(Long id);

    List<ComentarioDTO> getAllComentarios(Long id);

    boolean exists(Long id);
}
