package com.bibliotech.service;

import com.bibliotech.entity.Notificacion;
import com.bibliotech.entity.TipoNotificacion;
import com.bibliotech.repository.NotificacionRepository;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
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
    AuthenticationService authenticationService;
    @Override
    public List<Notificacion> getUserNotifications(){
        User usuario = authenticationService.getActiveUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error con el usuario loggeado"));
        return notificacionRepository.findByUser(usuario);
    }

    @Override
    public List<Notificacion> getUserUnreadNotifications(){
        User usuario = authenticationService.getActiveUser().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error con el usuario loggeado"));
        return notificacionRepository.findByUserAndLeidoIsFalse(usuario);
    }

    @Override
    public void crearNotificacion(User usuario, String mensaje, TipoNotificacion tipoNotificacion) {
        notificacionRepository.save(Notificacion.builder()
                        .user(usuario)
                        .tipoNotificacion(tipoNotificacion)
                        .mensaje(mensaje)
                        .leido(false)
                .build());
    }
}
