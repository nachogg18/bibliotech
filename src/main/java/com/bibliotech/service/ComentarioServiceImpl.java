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
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {
    
    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    AuthenticationService authenticationService;

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
    public Comentario delete(Long id){
        Optional<User> user = authenticationService.getActiveUser();
        if (!user.isPresent()){
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "No se encontro el usuario de la sesion activa");
        }
        Comentario comentario = comentarioRepository.findById(id).orElseThrow(
                () -> new HttpClientErrorException(HttpStatus.BAD_REQUEST,"No existe comentario con el id propocionado")
        );
        comentario.setFechaBaja(Instant.now());
        comentario.setBajaUsuario(user.get());
        comentarioRepository.save(comentario);
        return comentario;
    }

    @Override
    public Optional<Comentario> findById(Long id) {
        return comentarioRepository.findById(id);
    }
}
