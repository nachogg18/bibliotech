package com.bibliotech.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailSendRequest(
        @Email
        String to, 
        @NotBlank
        String subject, 
        @NotBlank String text) {}
