package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.request.SignUpWithoutRequiredConfirmationRequest;
import com.bibliotech.security.dao.request.SigninRequest;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
import com.bibliotech.security.entity.VerificationCode;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.UserVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private final UserVerificationService userVerificationService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signup-without-verfication")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'USER')")
    public ResponseEntity signupWithoutVerification(@RequestBody @Valid SignUpWithoutRequiredConfirmationRequest request) {
        
        return ResponseEntity.ok(authenticationService.signupWithoutRequiredConfirmation(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/verify")
    public ResponseEntity verifyAccount(@RequestBody @Valid VerificationCode verificationCode) {

        if (userVerificationService.verifyCodeForEmail(
                verificationCode.getEmail(), verificationCode.getCode())) {
            return ResponseEntity.ok("usuario activado correctamente");
        } else {
            return ResponseEntity.ok("Falló la verificación del código");
        }
    }
}