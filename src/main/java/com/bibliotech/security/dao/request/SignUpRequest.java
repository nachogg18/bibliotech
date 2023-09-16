package com.bibliotech.security.dao.request;
public record SignUpRequest(
    String firstName,
    String lastName,
    String email,
    String password,
    String rol
){}