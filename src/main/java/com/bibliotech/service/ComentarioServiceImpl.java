package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.repository.ComentarioRepository;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {
    private final ComentarioRepository comentarioRepository;

    @Override
    public List<Comentario> findAll() {
        return comentarioRepository.findByFechaBajaNull()
                .stream().toList();
    }

    @Override
    public Comentario save(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    @Override
    public Comentario edit(Comentario request, Long id) {
    Comentario comentario =
        comentarioRepository
            .findById(id)
            .orElseThrow(() -> new ValidationException("comentario not found"));
    
        comentario.setComentario(comentario.getComentario());
        comentario.setCalificacion(comentario.getCalificacion());
        return comentarioRepository.save(comentario);
    }

    @Override
    public Comentario delete(Long id) {
        Comentario comentario = comentarioRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );
        comentario.setId(id);
        comentario.setFechaBaja(Instant.now());
        return comentarioRepository.save(comentario);
    }

    @Override
    public Optional<Comentario> findById(Long id) {
        return comentarioRepository.findById(id);
    }

}
