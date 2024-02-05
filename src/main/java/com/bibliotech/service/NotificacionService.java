package com.bibliotech.service;

import com.bibliotech.entity.Notificacion;
import com.bibliotech.entity.TipoNotificacion;
import java.util.List;

public interface NotificacionService {
    List<Notificacion> getUserNotifications();

    List<Notificacion> getUserUnreadNotifications();

    void crearNotificacion(Long usuarioID, String mensaje, TipoNotificacion tipoNotificacion);

    Notificacion setNotificacionRead(Long id);

    Long count();
}
