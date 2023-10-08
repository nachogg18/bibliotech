package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.dao.response.GetUserInfoResponse;
import com.bibliotech.security.dao.response.RoleDto;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired private final AuthenticationService authenticationService;

  @PreAuthorize("@authenticationService.authentication.authenticated")
  @GetMapping("/get-active-user-info")
  public ResponseEntity<GetUserInfoResponse> getActiveUserInfo() {

    User user = authenticationService.getActiveUser();

    return ResponseEntity.ok(
        GetUserInfoResponse.builder()
            .userName(user.getUsername())
            .email(user.getEmail())
            .userId(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .roles(
                user.getRoles().stream()
                    .map(
                        role ->
                            RoleDto.builder().roleId(role.getId()).roleName(role.getName()).build())
                    .collect(Collectors.toList()))
            .build());
  }
}
