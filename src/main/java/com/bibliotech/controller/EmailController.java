package com.bibliotech.controller;

import com.bibliotech.dto.EmailSendRequest;
import com.bibliotech.service.EmailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/enviar-correo")
public class EmailController {
    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'ROLE')")
    public ResponseEntity<String> enviarCorreo(@RequestBody @Valid EmailSendRequest emailSendRequest) {
        emailService.sendEmail(emailSendRequest.to(), emailSendRequest.subject(), emailSendRequest.text());
        return ResponseEntity.ok("Correo enviado con éxito");
    }
}
