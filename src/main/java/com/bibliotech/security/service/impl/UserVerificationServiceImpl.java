package com.bibliotech.security.service.impl;

import com.bibliotech.security.entity.User;
import com.bibliotech.security.entity.VerificationCode;
import com.bibliotech.security.repository.VerificationCodeRepository;
import com.bibliotech.security.service.UserService;
import com.bibliotech.security.service.UserVerificationService;
import com.bibliotech.service.EmailService;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserVerificationServiceImpl implements UserVerificationService {

    private final UserService userService;

    private final EmailService emailService;

    private final VerificationCodeRepository verificationCodeRepository;

    public boolean verifyCodeForEmail(String email, String code) {
        Optional<User> user = userService.findByEmail(email);

        if (user.isEmpty()) {
            return false;
        }

        if (Objects.nonNull(user.get().getConfirmationDate())) {
            return false;
        }


        Optional<VerificationCode> verificationCode = verificationCodeRepository.findByCode(code);

        if (verificationCode.isEmpty()) {
            return false;
        }

        if (Objects.nonNull(verificationCode.get().getVerificationDate())) {
            return false;
        }

        if (Objects.nonNull(verificationCode.get().getExpirationDate()) && verificationCode.get().getExpirationDate().isBefore(Instant.now())) {
            return false;
        }
        
        

        Optional<VerificationCode> codeVerified = verificationCode.filter(
                vc -> vc.getEmail().equals(email)
        ).filter(
                vc -> vc.getCode().equals(code)
        );

        if (codeVerified.isPresent()) {
            Instant verificationDate = Instant.now();
            codeVerified.get().setVerificationDate(verificationDate);
            verificationCodeRepository.save(codeVerified.get());
            user.get().setConfirmationDate(verificationDate);

            userService.save(user.get());

            return true;
        }
        return false;
    }


    public static String generateVerificationCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").substring(0, 6);
    }

    public VerificationCode createVerificationCode(String email) {
        List<VerificationCode> previousVerificationCodes = verificationCodeRepository.findByEmail(email);

        previousVerificationCodes.stream().forEach(
                verificationCode -> invalidateVerificationCode(verificationCode)
        );

        String code = generateVerificationCode();

        Instant expirationInstant = Instant.now().atZone(ZoneId.systemDefault()).plusMinutes(30).toInstant();

        VerificationCode verificationCode = VerificationCode.builder()
                .creationDate(Instant.now())
                        .code(code)
                                .email(email)
                                        .verificationDate(null)
                                                .expirationDate(expirationInstant)
                                                        .build();

        return verificationCodeRepository.save(verificationCode);
    }

    @Override
    public void sendVerificationCode(VerificationCode verificationCode) {
        emailService.sendEmail(verificationCode.getEmail(), "Confirmar cuenta con el siguiente codigo de verificacion",
                "Confirma tu cuenta con el siguiente código de verificacion: "+verificationCode.getCode()

                );
    }

    @Override
    public void invalidateVerificationCode(VerificationCode verificationCode) {
        verificationCode.setExpirationDate(Instant.now());
    }

    @Override
    public void bypassVerification(User user) {
        user.setConfirmationDate(Instant.now());
        userService.save(user);
    }

}
