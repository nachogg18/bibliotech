package com.bibliotech.service;

import com.bibliotech.entity.*;
import com.bibliotech.repository.NotificacionRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    PrestamosRepository prestamosRepository;

    private static final Logger logger = LoggerFactory.getLogger(NotificacionServiceImpl.class);

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
        notificacionRepository.save(Notificacion.builder()
                        .userID(usuarioID)
                        .mensaje(mensaje)
                        .tipoNotificacion(tipoNotificacion)
                        .fecha(Instant.now().truncatedTo(ChronoUnit.HOURS))
                        .leido(false)
                .build());
    }

    @Override
    public Notificacion setNotificacionRead(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe notificación con id %s", id)));
        notificacion.setLeido(true);
        return notificacionRepository.save(notificacion);
    }

    @Scheduled(cron = "0 0 8 * * *") // 8am todos los dias
    public void crearNotificacionDiaPrevioAFinPrestamo() {
        logger.info("Iniciando proceso de creación de notificaciones de préstamos a vencer automático.");
        List<Prestamo> prestamos = prestamosRepository.findAllByIntervalFechaFinAndFechaBajaNull(Instant.now().truncatedTo(ChronoUnit.DAYS),Instant.now().plus(1, ChronoUnit.DAYS));
        for (Prestamo prestamo : prestamos) {
            System.out.println(prestamo.getId());
            crearNotificacion(
                    prestamo.getUsuario().getId(),
                    String.format("Préstamo con publicación %s vence mañana", prestamo.getEjemplar().getPublicacion().getTitulo()),
                    TipoNotificacion.PRESTAMO_POR_VENCER
            );
        }
        logger.info("Proceso de creación de notificaciones de préstamos a vencer automático finalizado. {} notificaciones creadas.",prestamos.size());
    }
}
