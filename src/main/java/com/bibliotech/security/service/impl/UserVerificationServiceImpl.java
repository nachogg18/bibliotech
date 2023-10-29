package com.bibliotech.security.service.impl;

import com.bibliotech.notifications.entity.Notification;
import com.bibliotech.notifications.service.NotificationService;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.entity.VerificationCode;
import com.bibliotech.security.repository.VerificationCodeRepository;
import com.bibliotech.security.service.UserService;
import com.bibliotech.security.service.UserVerificationService;
import com.bibliotech.utils.Dupla;
import com.bibliotech.utils.Tripla;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserVerificationServiceImpl implements UserVerificationService {

    private final UserService userService;

    private final VerificationCodeRepository verificationCodeRepository;

    private final NotificationService notificationService;

    public Dupla<Boolean, Optional<User>> verifyCodeForRegister(String email, String code) {
        Optional<User> getUserToConfirm = userService.getUserToConfirmByEmail(email);
        var resultConfirmedVerificationCode = verifyCode(getUserToConfirm, code);

        var enableUserOperationResult = enableUser(
                resultConfirmedVerificationCode.getSegundo(),
                resultConfirmedVerificationCode.getTercero());

        return new Dupla<>(enableUserOperationResult.getPrimero(),
                enableUserOperationResult.getTercero()
                ) ;

    }

    public Tripla<Boolean, Optional<VerificationCode>, Optional<User>> verifyCode(Optional<User> optionalUser, String code) {
        if (optionalUser.isEmpty()) {
            return new Tripla<>(Boolean.FALSE, Optional.of(null), optionalUser);
        }

        Dupla<Boolean, Optional<VerificationCode>> resultCheckedVerificationCode = checkVerificationCode(code, optionalUser.get().getEmail());

        if (!resultCheckedVerificationCode.getPrimero()) {
            return new Tripla<>(false, resultCheckedVerificationCode.getSegundo(), optionalUser)  ;
        }

        Dupla<Boolean, Optional<VerificationCode>> resultConfirmedVerificationCode = confirmVerificationCode(resultCheckedVerificationCode.getSegundo());


        return new Tripla<>(resultConfirmedVerificationCode.getPrimero(), resultConfirmedVerificationCode.getSegundo(), optionalUser);

    }

    private Dupla<Boolean, Optional<VerificationCode>> checkVerificationCode(String code, String email) {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findByCode(code);

        if (verificationCode.isEmpty()) {
            return new Dupla<>(Boolean.FALSE, verificationCode);
        }

        if (Objects.nonNull(verificationCode.get().getVerificationDate())) {
            return new Dupla<>(Boolean.FALSE, verificationCode);
        }

        if (Objects.nonNull(verificationCode.get().getExpirationDate()) && verificationCode.get().getExpirationDate().isBefore(Instant.now())) {
            return new Dupla<>(Boolean.FALSE, verificationCode);
        }


        Optional<VerificationCode> codeVerified = verificationCode.filter(
                vc -> vc.getEmail().equals(email)
        ).filter(
                vc -> vc.getCode().equals(code)
        );

        if (codeVerified.isEmpty()) {
            return new Dupla<>(Boolean.FALSE, codeVerified);
        }

        return new Dupla<>(Boolean.TRUE, codeVerified);
    }

    private Dupla<Boolean, Optional<VerificationCode>> confirmVerificationCode(Optional<VerificationCode> verificationCode) {
        if (verificationCode.isPresent()) {
            Instant verificationDate = Instant.now();
            verificationCode.get().setVerificationDate(verificationDate);
            verificationCodeRepository.save(verificationCode.get());
            return new Dupla<>(Boolean.TRUE, verificationCode);
        }


        return new Dupla<>(Boolean.FALSE, verificationCode);

    }

    private Tripla<Boolean, Optional<VerificationCode>, Optional<User>>  enableUser(Optional<VerificationCode> confirmedVerificationCode, Optional<User> user) {
            if (user.isEmpty()) {
                return new Tripla<>(Boolean.FALSE, confirmedVerificationCode, user);
            }

            if (confirmedVerificationCode.isEmpty()) {
                return new Tripla<>(Boolean.FALSE, confirmedVerificationCode, user);
            }

            if (Objects.isNull(confirmedVerificationCode.get().getVerificationDate())) {
                return new Tripla<>(Boolean.FALSE, confirmedVerificationCode, user);
            }

            user.get().setConfirmationDate(confirmedVerificationCode.get().getVerificationDate());

            userService.save(user.get());

            return new Tripla<>(Boolean.TRUE, confirmedVerificationCode, user);

    }

    @Override
    public Dupla<Boolean, Optional<User>> verifyCodeForResetPassword(String email, String code) {
        Optional<User> optionalUser = userService.getActiveUserByEmail(email);
        Tripla<Boolean, Optional<VerificationCode>, Optional<User>> verificationCodeOperationResult = verifyCode(optionalUser, code);

        return new Dupla<>(verificationCodeOperationResult.getPrimero(), verificationCodeOperationResult.getTercero());
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
    public void sendVerificationCode(VerificationCode verificationCode, String subject, String message, String recipient) {

        Notification notification = Notification.builder()
                .subject(subject)
                .message(message+verificationCode.getCode())
                .recipient(recipient)
                .build();

        notificationService.notifyUser(notification);
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
