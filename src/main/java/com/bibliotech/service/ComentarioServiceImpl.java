package com.bibliotech.service;

import com.bibliotech.dto.ComentarioDTO;
import com.bibliotech.dto.CrearComentarioDTO;
import com.bibliotech.entity.*;
import com.bibliotech.repository.ComentarioRepository;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {
    
    @Autowired
    ComentarioRepository comentarioRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    EjemplarService ejemplarService;

    @Autowired
    PublicacionService publicacionService;


    @Override
    public List<ComentarioDTO> findAll() {
        return comentarioRepository.findByFechaBajaNull()
                .stream().map(comentario -> {
                    ComentarioDTO dto = new ComentarioDTO();
                    dto.setId(comentario.getId());
                    dto.setComentario(comentario.getComentario());

                    Instant fecha = comentario.getFechaAlta();
                    ZoneId zonaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
                    // Crear un formateador de fecha y hora
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    // Formatear el Instant en la zona horaria de Argentina
                    String formattedDateTime = fecha.atZone(zonaArgentina).format(formatter);

                    dto.setFecha(formattedDateTime);
                    dto.setCalificacion(comentario.getCalificacion());
                    dto.setAltaUsuario(comentario.getAltaUser().getFirstName() + ' ' + comentario.getAltaUser().getLastName());
                    return dto;
                }).toList();
    }

    @Override
    public ComentarioDTO saveComentarioEjemplar(CrearComentarioDTO req, Long idEjemplar) {
        Ejemplar ejemplar = ejemplarService.findById(idEjemplar).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe ejemplar con el id %s",idEjemplar)));

        Comentario comentarioNuevo = Comentario.builder()
                .comentario(req.getComentario())
                .calificacion(req.getCalificacion())
                .fechaAlta(Instant.now())
                .altaUser(authenticationService.getActiveUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error con el usuario")))
                .build();
        comentarioRepository.save(comentarioNuevo);

        ejemplar.getComentarios().add(comentarioNuevo);
        ejemplarService.save(ejemplar);

        return ComentarioDTO.builder()
                .altaUsuario(comentarioNuevo.getAltaUser().getFirstName() + ' ' + comentarioNuevo.getAltaUser().getLastName())
                .comentario(comentarioNuevo.getComentario())
                .id(comentarioNuevo.getId())
                .fecha(comentarioNuevo.getFechaAlta().atZone(ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .calificacion(comentarioNuevo.getCalificacion())
                .build();
    }

    @Override
    public ComentarioDTO saveComentarioPublicacion(CrearComentarioDTO req, Long idPublicacion) {
        Publicacion publicacion = publicacionService.findById(idPublicacion).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe publicacion con el id %s",idPublicacion)));

        Comentario comentarioNuevo = Comentario.builder()
                .comentario(req.getComentario())
                .calificacion(req.getCalificacion())
                .fechaAlta(Instant.now())
                .altaUser(authenticationService.getActiveUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error con el usuario")))
                .build();
        comentarioRepository.save(comentarioNuevo);

        publicacion.getComentarios().add(comentarioNuevo);
        publicacionService.save(publicacion);

        return ComentarioDTO.builder()
                .altaUsuario(comentarioNuevo.getAltaUser().getFirstName() + ' ' + comentarioNuevo.getAltaUser().getLastName())
                .comentario(comentarioNuevo.getComentario())
                .id(comentarioNuevo.getId())
                .fecha(comentarioNuevo.getFechaAlta().atZone(ZoneId.of("America/Argentina/Buenos_Aires")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .calificacion(comentarioNuevo.getCalificacion())
                .build();
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
        comentario.setBajaUser(user.get());
        comentarioRepository.save(comentario);
        return comentario;
    }

    @Override
    public Optional<Comentario> findById(Long id) {
        return comentarioRepository.findById(id);
    }

    @Override
    public Comentario save(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    @Override
    public List<ComentarioDTO> findByUserId(Long id) {
        List<Comentario> comentarios = comentarioRepository.findByAltaUserId(id);

        return comentarios.stream().map(comentario -> {
            ComentarioDTO dto = new ComentarioDTO();
            dto.setId(comentario.getId());
            dto.setComentario(comentario.getComentario());

            Instant fecha = comentario.getFechaAlta();
            ZoneId zonaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
            // Crear un formateador de fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            // Formatear el Instant en la zona horaria de Argentina
            String formattedDateTime = fecha.atZone(zonaArgentina).format(formatter);

            dto.setFecha(formattedDateTime);
            dto.setCalificacion(comentario.getCalificacion());
            dto.setAltaUsuario(comentario.getAltaUser().getFirstName() + ' ' + comentario.getAltaUser().getLastName());
            return dto;
        }).toList();
    }

    @Override
    public List<ComentarioDTO> findByEjemplarId(Long id) {
        return ejemplarService.getAllComentarios(id);

    }

    @Override
    public List<ComentarioDTO> findByPublicacionId(Long id) {
        return publicacionService.getAllComentarios(id);

    }
}
