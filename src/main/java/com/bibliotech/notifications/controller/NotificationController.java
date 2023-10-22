package com.bibliotech.notifications.controller;

import com.bibliotech.dto.EmailSendRequest;
import com.bibliotech.notifications.service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping(path = "/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    
    private final EmailService emailService;

    @PostMapping("")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'NOTIFICATION')")
    public ResponseEntity<String> enviarCorreo(@RequestBody @Valid EmailSendRequest emailSendRequest) {
        emailService.sendEmail(emailSendRequest.to(), emailSendRequest.subject(), emailSendRequest.text());
        return ResponseEntity.ok("Correo enviado con éxito");
    }
}
