package com.bibliotech.security.dao.response;

import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private List<String> roles;
    private String startDate;
    private String lastUpdatedDate;
    private String endDate;
    private String confirmationDate;
    private String dni;
    private String legajo;


    public static UserDetailDto userToUserDetailDto(User user) {
    return UserDetailDto.builder()
        .id(Objects.nonNull(user.getId()) ? user.getId() : Long.valueOf(0))
        .nombre(Objects.nonNull(user.getFirstName()) ? user.getFirstName() : "")
        .apellido(Objects.nonNull(user.getLastName()) ? user.getLastName() : "")
        .email(Objects.nonNull(user.getEmail()) ? user.getEmail() : "")
        .dni(Objects.nonNull(user.getDni()) ? user.getDni() : "")
        .legajo(Objects.nonNull(user.getDni()) ? user.getLegajo() : "")
        .startDate(user.getStartDate().toString())
        .lastUpdatedDate(
            Objects.nonNull(user.getLastUpdatedDate()) ? user.getLastUpdatedDate().toString() : "")
        .endDate(Objects.nonNull(user.getEndDate()) ? user.getEndDate().toString() : "")
        .confirmationDate(
            Objects.nonNull(user.getConfirmationDate())
                ? user.getConfirmationDate().toString()
                : "")
        .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
        .build();
    }
}