package com.bibliotech.service;

import com.bibliotech.entity.Comentario;
import com.bibliotech.repository.ComentarioRepository;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService{

    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    AuthenticationService authenticationService;

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
}
