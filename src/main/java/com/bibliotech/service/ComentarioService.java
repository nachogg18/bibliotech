package com.bibliotech.service;

import com.bibliotech.dto.ComentarioDTO;
import com.bibliotech.dto.CrearComentarioDTO;
import com.bibliotech.entity.Comentario;
import java.util.List;
import java.util.Optional;

public interface ComentarioService {
    List<ComentarioDTO> findAll();

    Optional<Comentario> findById(Long id);

    Comentario save(Comentario comentario);

    ComentarioDTO saveComentarioEjemplar(CrearComentarioDTO req, Long idEjemplar);

    ComentarioDTO saveComentarioPublicacion(CrearComentarioDTO req, Long idPublicacion);

    Comentario edit(Comentario comentario, Long id);

    Comentario delete(Long id);

    List<ComentarioDTO> findByUserId(Long id);

    List<ComentarioDTO> findByEjemplarId(Long id);

    List<ComentarioDTO> findByPublicacionId(Long id);

    Long count();
}
