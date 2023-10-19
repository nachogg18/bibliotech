package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.request.SigninRequest;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
import com.bibliotech.security.service.AuthenticationService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignUpRequest request) {

        if (Objects.nonNull(request.roleIds())) {
            if (!authenticationService.hasPrivilegeOfDoActionForResource("CREATE", "USER")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        }
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}