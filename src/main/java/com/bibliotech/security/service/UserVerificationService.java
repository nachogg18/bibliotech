package com.bibliotech.security.service;

import com.bibliotech.security.entity.User;
import com.bibliotech.security.entity.VerificationCode;
import com.bibliotech.utils.Dupla;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserVerificationService {
    Dupla<Boolean, Optional<User>> verifyCodeForRegister(String email, String code);

    Dupla<Boolean, Optional<User>> verifyCodeForResetPassword(String email, String code);

    VerificationCode createVerificationCode(String email);

    void sendVerificationCode(VerificationCode verificationCode, String subject, String message, String recipient);

    void invalidateVerificationCode(VerificationCode verificationCode);

    void bypassVerification(User user);
}
