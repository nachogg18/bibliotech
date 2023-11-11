package com.bibliotech.security.service;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
import com.bibliotech.security.dao.response.ResetUserPasswordResponse;
import com.bibliotech.security.entity.User;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    User signupRequiredConfirmation(SignUpRequiredConfirmationRequest request);
    User signupWithoutRequiredConfirmation(@Valid SignUpWithoutRequiredConfirmationRequest request);
    User setNewPassword(User user, String password);
    JwtAuthenticationResponse signin(SigninRequest request);
    Optional<User> getActiveUser();
    Authentication getAuthentication();
    ResetUserPasswordResponse resetUserPassword(ResetUserPasswordRequest request);
    User setNewUserPassword(NewUserPasswordRequest request);
    Boolean hasPrivilegeOfDoActionForResource(String actionName, String resourceName);
}