package com.bibliotech.controller;

import com.bibliotech.entity.Notificacion;
import com.bibliotech.service.NotificacionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/notificaciones")
@SecurityRequirement(name= "bearer-key")
public class NotificacionController {

    @Autowired
    NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<Notificacion>> getUserNotifications(){
        return ResponseEntity.ok().body(notificacionService.getUserNotifications());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notificacion>> getUserUnreadNotifications(){
        return ResponseEntity.ok().body(notificacionService.getUserUnreadNotifications());
    }

    @PostMapping("{id}/read")
    public ResponseEntity<Notificacion> setNotificacionRead(@PathVariable Long id) {
        return ResponseEntity.ok().body(notificacionService.setNotificacionRead(id));
    }
}
