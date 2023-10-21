package com.bibliotech.security.controller;

import com.bibliotech.security.service.UserVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserVerificationController {


    @Autowired
    private UserVerificationService userVerificationService;

    @GetMapping("/verify")
    public ResponseEntity verifyAccount(String email, String code) {

        if (userVerificationService.verifyCodeForEmail(email, code)) {
            return ResponseEntity.ok("usuario activado correctamente");
        } else {
            return ResponseEntity.ok("Falló la verificación del código");
        }
    }
}

