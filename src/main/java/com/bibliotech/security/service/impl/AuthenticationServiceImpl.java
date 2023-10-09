package com.bibliotech.security.service.impl;

import com.bibliotech.security.dao.request.SignUpRequest;
import com.bibliotech.security.dao.request.SigninRequest;
import com.bibliotech.security.dao.response.JwtAuthenticationResponse;
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
import jakarta.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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


      List<Role> roles = new ArrayList<>();
      
      
      if (request.roleIds() == null || request.roleIds().isEmpty() ) {
          
      logger.error("sin role id en la request, asignando role por defecto");
       var basicRole = roleService.findByNameAndEndDateNull(RoleUtils.DEFAULT_ROLE_USER).orElseThrow(() -> new ValidationException(String.format("no existen roles activos con ese nombre %s", RoleUtils.DEFAULT_ROLE_USER)));
       roles.add(basicRole);
      } else {
          request.roleIds().stream().map(
                  roleId -> roleService.findByIdAndEndDateNull(roleId).orElseThrow(() -> new ValidationException(String.format("no existen roles activos con id %s", roleId)))
          ).forEach( role -> roles.add(role));
      }


    User savedUser = userRepository.save(User.builder()
            .firstName(request.firstName())
            .lastName(request.lastName())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .roles(roles)
            .build());
    roles.stream().forEach(role -> roleService.assignUserToRol(role.getId(), savedUser));
    var jwtToken = jwtService.generateToken(savedUser);
    var refreshToken = jwtService.generateRefreshToken(savedUser);
    revokeAllUserTokens(savedUser);
    saveUserToken(savedUser, jwtToken);
    return JwtAuthenticationResponse.builder().token(jwtToken).refreshToken(refreshToken).build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token =
        Token.builder()
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
    if (validUserTokens.isEmpty()) return;
    validUserTokens.forEach(
        token -> {
          token.setExpired(true);
          token.setRevoked(true);
        });
    tokenRepository.saveAll(validUserTokens);
  }

  @Override
  public JwtAuthenticationResponse signin(SigninRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    var user =
        userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
    var jwt = jwtService.generateToken(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return JwtAuthenticationResponse.builder().token(jwt).refreshToken(refreshToken).build();
  }

  @Override
  public User getActiveUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public Boolean hasPrivilegeOfDoActionForResource(String actionName, String resourceName) {

    return Stream.of(getActiveUser())
        .filter(user -> user.getEndDate() == null)
        .findAny()
        .orElseThrow(() -> new ValidationException("user disabled"))
        .getRoles()
        .stream()
        .filter(role -> role.getEndDate() == null)
        .flatMap(role -> role.getPrivileges().stream())
        .filter(privilege -> privilege.getEndDate() == null)
        .filter(privilege -> resourceName.equals(privilege.getResource().getName()))
        .anyMatch(
            privilege ->
                privilege.getActions().stream()
                    .anyMatch(action -> actionName.equals(action.name())));
  }
}
