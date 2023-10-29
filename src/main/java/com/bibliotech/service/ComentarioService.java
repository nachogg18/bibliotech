package com.bibliotech.service;

import com.bibliotech.entity.Comentario;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ComentarioService {

    @Transactional
    Comentario delete(Long id);
}
