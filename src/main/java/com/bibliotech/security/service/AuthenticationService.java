package com.bibliotech.security.service;

import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.request.SigninRequest;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
import com.bibliotech.security.entity.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    Optional<User> getActiveUser();
    Authentication getAuthentication();

    Boolean hasPrivilegeOfDoActionForResource(String actionName, String resourceName);
}