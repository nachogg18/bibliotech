package com.bibliotech.security.controller;

import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.dao.response.GetUserInfoResponse;
import com.bibliotech.security.dao.response.RoleDto;
import com.bibliotech.security.dao.response.UserDetailDto;
import com.bibliotech.security.dao.response.UserDto;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.security.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.stream.Streams;
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

  @Autowired private final RoleService roleService;

  @Autowired private final UserService userService;

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

  @PostMapping("/assign-roles")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('UPDATE', 'USER')")
  public ResponseEntity<AssignRoleRequest> assignRoles(
      @RequestBody @Valid AssignRoleRequest request) {
    Optional<User> user = userService.findById(request.userId());

    return ResponseEntity.ok(
        new AssignRoleRequest(
            user.get().getId(),
            request.roleIds().stream()
                .map(
                    roleId -> {
                      Role role = roleService.assignUserToRol(roleId, user.get());
                      user.get().getRoles().add(role);
                      userService.save(user.get());
                      return roleId;
                    })
                .collect(Collectors.toList())));
  }

  @PutMapping("/remove-roles")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('UPDATE', 'USER')")
  public ResponseEntity<AssignRoleRequest> removeRoles(
      @RequestBody @Valid AssignRoleRequest request) {
    Optional<User> user = userService.findById(request.userId());

    return ResponseEntity.ok(
        new AssignRoleRequest(
            user.get().getId(),
            request.roleIds().stream()
                .map(
                    roleId -> {
                      Role role = roleService.removeUserToRol(roleId, user.get());
                      user.get().getRoles().remove(role);
                      userService.save(user.get());
                      return roleId;
                    })
                .collect(Collectors.toList())));
  }


    @GetMapping("")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'USER')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(
            user -> UserDto.builder()
                    .id((Objects.nonNull(user.getId())) ? user.getId() : 0)
                    .nombre(user.getFirstName() + user.getLastName())
                    .roles((Objects.nonNull(user.getRoles()) ? user.getRoles().stream().map(Role::getName).collect(Collectors.toList()) : List.of())).build()
                            
        ).collect(Collectors.toList()));
    }


    @GetMapping("/{id}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'USER')")
    public ResponseEntity<UserDetailDto> getUserDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.findById(id).map(
                        user -> UserDetailDto.builder()
                                .id((Objects.nonNull(user.getId())) ? user.getId() : 0)
                                .nombre(user.getFirstName())
                                .apellido(user.getLastName())
                                .email((Objects.nonNull(user.getEmail())) ? user.getEmail() : "")
                                .roles((Objects.nonNull(user.getRoles()) ? user.getRoles().stream().map(Role::getName).collect(Collectors.toList()) : List.of()))
                                .build()
                ).get()
        );
    }


    @PutMapping("{userId}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'USER')")
    public ResponseEntity<UserDetailDto> editUser(@PathVariable @Valid @NotNull Long userId, @RequestBody @Valid EditUserRequest editUserRequest) {

    if (authenticationService.getActiveUser().getId() == userId) {
        throw new ValidationException("El usuario no puede modificar sus propios datos");
    }



    return ResponseEntity.ok(
            Streams.of(userService.edit(userId, editUserRequest)).map(
                    user ->
                        UserDetailDto.builder()
                                .id(user.getId())
                                .nombre(user.getFirstName())
                                .apellido(user.getLastName())
                                .email(user.getEmail())
                                .roles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()))
                                .build()
            ).findAny().get()

        );

    }
}
