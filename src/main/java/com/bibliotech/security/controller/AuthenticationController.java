package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
import com.bibliotech.security.dao.response.UserDetailDto;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.entity.VerificationCode;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.UserVerificationService;
import com.bibliotech.utils.Dupla;
import jakarta.validation.Valid;
import java.util.Optional;
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
    public ResponseEntity<UserDetailDto> signup(@RequestBody @Valid SignUpRequiredConfirmationRequest request) {

        return ResponseEntity.ok(UserDetailDto.userToUserDetailDto(authenticationService.signupRequiredConfirmation(request)));
    }

    @PostMapping("/signup-without-verfication")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('CREATE', 'USER')")
    public ResponseEntity signupWithoutVerification(@RequestBody @Valid SignUpWithoutRequiredConfirmationRequest request) {
        
        return ResponseEntity.ok(UserDetailDto.userToUserDetailDto(authenticationService.signupWithoutRequiredConfirmation(request)));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody @Valid SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @PostMapping("/verify")
    public ResponseEntity resetUserPassword(@RequestBody @Valid VerificationCode verificationCode) {

        Dupla<Boolean, Optional<User>> verifyCodeForRegisterResult = userVerificationService.verifyCodeForRegister(
                verificationCode.getEmail(),
                verificationCode.getCode());

        if (verifyCodeForRegisterResult.getPrimero()) {
            return ResponseEntity.ok(UserDetailDto.userToUserDetailDto(verifyCodeForRegisterResult.getSegundo().get()));
        }

        return ResponseEntity.ok(verifyCodeForRegisterResult.getPrimero());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity resetUserPassword(@RequestBody @Valid ResetUserPasswordRequest request) {

        return ResponseEntity.ok(authenticationService.resetUserPassword(request));
    }

    @PostMapping("/generate-activation-code")
    public ResponseEntity generateVerificationCode(@RequestBody @Valid ResetUserPasswordRequest request) {

        return ResponseEntity.ok(authenticationService.generateVerificationCode(request));
    }

    @PostMapping("/confirm-new-password")
    public ResponseEntity<UserDetailDto> confirmNewPassword(@RequestBody @Valid NewUserPasswordRequest request) {

        User user  = authenticationService
                .setNewUserPassword(request);

        return ResponseEntity.ok(UserDetailDto.userToUserDetailDto(user));
    }
}