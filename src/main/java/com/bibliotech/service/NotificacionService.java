package com.bibliotech.service;

import com.bibliotech.entity.Notificacion;
import com.bibliotech.entity.TipoNotificacion;
import com.bibliotech.security.entity.User;

import java.util.List;

public interface NotificacionService {
    List<Notificacion> getUserNotifications();

    List<Notificacion> getUserUnreadNotifications();

    void crearNotificacion(User usuario, String mensaje, TipoNotificacion tipoNotificacion);

    Notificacion setNotificacionRead(Long id);
}
