package com.bibliotech.security.service;

import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.request.SigninRequest;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    Boolean hasAdminRole();
}