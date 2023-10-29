package com.bibliotech.service;

import com.bibliotech.entity.Comentario;
import java.util.List;
import java.util.Optional;

public interface ComentarioService {
    List<Comentario> findAll();

    Optional<Comentario> findById(Long id);

    Comentario save(Comentario comentario);

    Comentario edit(Comentario comentario, Long id);

    Comentario delete(Long id);
}
