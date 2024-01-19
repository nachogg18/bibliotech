package com.bibliotech.service;

import com.bibliotech.entity.Notificacion;
import com.bibliotech.entity.TipoNotificacion;
import com.bibliotech.repository.NotificacionRepository;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService{

    @Autowired
    NotificacionRepository notificacionRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;
    @Override
    public List<Notificacion> getUserNotifications(){
        User usuario = authenticationService.getActiveUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error con el usuario loggeado"));
        return notificacionRepository.findByUserID(usuario.getId());
    }

    @Override
    public List<Notificacion> getUserUnreadNotifications(){
        User usuario = authenticationService.getActiveUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error con el usuario loggeado"));
        return notificacionRepository.findByUserIDAndLeidoIsFalse(usuario.getId());
    }

    @Override
    public void crearNotificacion(Long usuarioID, String mensaje, TipoNotificacion tipoNotificacion) {
        System.out.println("CrearNotificacion");
        notificacionRepository.save(Notificacion.builder()
                        .userID(usuarioID)
                        .tipoNotificacion(tipoNotificacion)
                        .mensaje(mensaje)
                        .leido(false)
                .build());
    }

    @Override
    public Notificacion setNotificacionRead(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe notificación con id %s", id)));
        notificacion.setLeido(true);
        return notificacionRepository.save(notificacion);
    }
}
