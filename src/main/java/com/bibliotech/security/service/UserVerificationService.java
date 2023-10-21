package com.bibliotech.security.service;

import com.bibliotech.security.entity.User;
import com.bibliotech.security.entity.VerificationCode;
import org.springframework.stereotype.Service;

@Service
public interface UserVerificationService {
    boolean verifyCodeForEmail(String email, String code);

    VerificationCode createVerificationCode(String email);

    void sendVerificationCode(VerificationCode verificationCode);

    void invalidateVerificationCode(VerificationCode verificationCode);

    void bypassVerification(User user);
}
