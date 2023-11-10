package com.bibliotech.controller;

import com.bibliotech.entity.Notificacion;
import com.bibliotech.service.NotificacionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<Notificacion>> getUserNotification(){
        return ResponseEntity.ok().body(notificacionService.getUserNotifications());
    }
}
