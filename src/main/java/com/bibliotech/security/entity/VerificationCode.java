package com.bibliotech.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationCode {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private Instant creationDate;
    private Instant expirationDate;
    private Instant verificationDate;
    private String email;
}