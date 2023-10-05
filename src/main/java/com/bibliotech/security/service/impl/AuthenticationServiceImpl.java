package com.bibliotech.security.service.impl;

import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.request.SigninRequest;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
import com.bibliotech.security.entity.Privilege;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.Token;
import com.bibliotech.security.entity.TokenType;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.repository.TokenRepository;
import com.bibliotech.security.repository.UserRepository;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.JwtService;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.utils.RoleUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("authenticationService")
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    @Override
    public JwtAuthenticationResponse signup(@Valid SignUpRequest request) {
        Optional<Role> role = roleService.findById(request.roleId());
        
        if (role.isEmpty()) {
            logger.error("el rol con id no fue encontrado");
            throw new RuntimeException("Recurso no encontrado");
        }

        User user = User.builder().firstName(request.firstName()).lastName(request.lastName())
                .email(request.email()).password(passwordEncoder.encode(request.password()))
                    .roles(List.of(role.stream().findAny().get())).build();
        var savedUser = user = userRepository.save(user);
        roleService.assignUserToRol(role.get().getId(), user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(savedUser, jwtToken);
        return JwtAuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .build();
    }

    public Boolean hasAdminRole() {
        User userAuthenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        return userAuthenticated.getRoles().stream().anyMatch(
                role -> RoleUtils.GetRolesWithAdminProfile()
                        .stream()
                        .anyMatch(
                                s -> {
                                    Boolean result = s.equals(role.getName());
                                    logger.debug(String.format("role_string: %s, match?: %s", s, result));
                                    return s.equals(role.getName());
                                }

                        )
        );
        
    }
    public Boolean hasSomeRoleOf(List<String> roleNames) {
        User userAuthenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userAuthenticated.getRoles().stream().anyMatch(
                role -> roleNames
                        .stream()
                        .anyMatch(
                                s -> {
                                    Boolean result = s.equals(role.getName());
                                    logger.debug(String.format("role_string: %s, match?: %s", s, result));
                                    return s.equals(role.getName());
                                }

                        )
        );

    }

    public Boolean hasPrivilegeOf(String privilegeName) {
        User userAuthenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userAuthenticated.getRoles().stream().flatMap(
                role -> role.getPrivileges().stream().map(
                        Privilege::getName
                )
        ).filter(
                privilegeName::equals
        ).findAny().isPresent();

    }

}