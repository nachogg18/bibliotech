package com.bibliotech.security.service.impl;

import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.request.SigninRequest;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
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
        user = userRepository.save(user);
        roleService.assignUserToRol(role.get().getId(), user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
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
}