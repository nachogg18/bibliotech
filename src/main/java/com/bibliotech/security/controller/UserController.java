package com.bibliotech.security.controller;

import com.bibliotech.dto.UserInfoDTO;
import com.bibliotech.security.dao.request.*;
import com.bibliotech.security.dao.response.*;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.RoleService;
import com.bibliotech.security.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

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

    User user = authenticationService.getActiveUser().orElseThrow(() -> new ValidationException("no authenticated user"));


    UserInfoDTO userInfoDTO = UserInfoDTO.toDto(user.getUserInfo());

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
            .legajo(userInfoDTO.getLegajo())
            .dni(userInfoDTO.getDni())
            .telefono(userInfoDTO.getTelefono())
            .direccion(userInfoDTO.getDireccionContacto())
            .localidad(userInfoDTO.getLocalidad())
            .facultad(userInfoDTO.getFacultad())
            .carrera(userInfoDTO.getCarrera())
            .build());
  }

  @PostMapping("/assign-roles")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('UPDATE', 'USER')")
  public ResponseEntity<AssignRoleRequest> assignRoles(@RequestBody @Valid AssignRoleRequest request) {
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
  public ResponseEntity<AssignRoleRequest> removeRoles(@RequestBody @Valid AssignRoleRequest request) {
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
                    .nombre(user.getFirstName())
                    .apellido(user.getLastName())
                    .email(user.getEmail())
                    .deshailitado(user.getEndDate())
                    .roles((Objects.nonNull(user.getRoles()) ? user.getRoles().stream().map(Role::getName).collect(Collectors.toList()) : List.of())).build()

        ).collect(Collectors.toList()));
  }


  @GetMapping("/{id}")
  @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'USER')")
  public ResponseEntity<UserDetailDto> getUserDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(
                userService.findById(id).map(
                        user -> UserDetailDto.userToUserDetailDto(user)
                ).get()
        );
  }

    @GetMapping("/dni")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'USER')")
    public ResponseEntity<List<String>> getUserDnis() {
        return ResponseEntity.ok(userService.getUsers().stream().map(findUserDto -> {
            return findUserDto.getDni();
        }).toList());
    }


    @PutMapping("{userId}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'USER')")
    public ResponseEntity<UserDetailDto> editUser(@PathVariable @Valid @NotNull Long userId, @RequestBody @Valid EditUserRequest editUserRequest) {

    if (authenticationService.getActiveUser().get().getId() == userId) {
        throw new ValidationException("El usuario no puede modificar sus propios datos");
    }



    return ResponseEntity.ok(
            Streams.of(userService.edit(userId, editUserRequest)).map(
                    user ->
                        UserDetailDto.userToUserDetailDto(user)
            ).findAny().get()

        );

    }

    @PutMapping("/edit/{userId}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('EDIT', 'USER')")
    public ResponseEntity<UserDetailDto> editOneUser(@PathVariable @Valid @NotNull Long userId, @RequestBody @Valid EditUserRequest editUserRequest){
      Optional<User> user = userService.findById(userId);
      if (!user.isPresent()) {
          throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User no enconcado con el id correspondiente");
      }
      user.get().setEmail(editUserRequest.email());
      user.get().setFirstName(editUserRequest.firstName());
      user.get().setLastName(editUserRequest.lastName());
      List<Role> rolesToAssing = new ArrayList<>();
      editUserRequest.roleIds().forEach(aLong -> {
          Role role = roleService.findOne(aLong);
          rolesToAssing.add(role);
      });
      user.get().setRoles(rolesToAssing);
      userService.save(user.get());
      return ResponseEntity.ok(UserDetailDto.builder()
                      .apellido(user.get().getLastName())
                      .nombre(user.get().getFirstName())
                      .email(user.get().getEmail())
                      .roles(user.get().getRoles().stream().map(role -> role.getName()).toList())
              .build());
    }

    @PutMapping("/edit/my")
    public ResponseEntity<UserDetailDto> editMyUser(@RequestBody @Valid EditUserRequest editUserRequest){
        Optional<User> user = authenticationService.getActiveUser();
        if (!user.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Usuario no encontrado.");
        }
        user.get().setEmail(editUserRequest.email());
        user.get().setFirstName(editUserRequest.firstName());
        user.get().setLastName(editUserRequest.lastName());
        if (Objects.nonNull(editUserRequest.password())){
            user.get().setPassword(editUserRequest.password());
        }
        List<Role> rolesToAssing = new ArrayList<>();
        editUserRequest.roleIds().forEach(aLong -> {
            Role role = roleService.findOne(aLong);
            rolesToAssing.add(role);
        });
        user.get().setRoles(rolesToAssing);
        userService.save(user.get());
        return ResponseEntity.ok(UserDetailDto.builder()
                .apellido(user.get().getLastName())
                .nombre(user.get().getFirstName())
                .email(user.get().getEmail())
                .roles(user.get().getRoles().stream().map(role -> role.getName()).toList())
                .build());
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('DELETE', 'USER')")
    public ResponseEntity<Boolean> deleteOneUser(@PathVariable @Valid @NotNull Long userId){
        Optional<User> user = userService.findById(userId);
        if (!user.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User no enconcado con el id correspondiente");
        }
        user.get().setEndDate(Instant.now());
        userService.save(user.get());
        return ResponseEntity.ok(true);
    }

    @GetMapping("/getUsers")
    @PreAuthorize("@authenticationService.hasPrivilegeOfDoActionForResource('READ', 'USER')")
    @SecurityRequirement(name = "bearer-key")
    public List<FindUserDto> getUsers() {
      return userService.getUsers();
    }
}
