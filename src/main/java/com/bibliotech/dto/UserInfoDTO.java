package com.bibliotech.dto;

import com.bibliotech.security.entity.UserInfo;
import java.util.Objects;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoDTO {
    private String legajo;
    private String dni;
    private String telefono;
    private String emailContacto;
    private String direccionContacto;
    private LocalidadDTO localidad;


    public static UserInfoDTO toDto(UserInfo userInfo) {
    return Objects.nonNull(userInfo)
        ? UserInfoDTO.builder()
            .dni(Objects.nonNull(userInfo.getDni()) ? userInfo.getDni() : "")
            .legajo(Objects.nonNull(userInfo.getLegajo()) ? userInfo.getLegajo() : "")
            .direccionContacto(Objects.nonNull(userInfo.getDireccionContacto()) ? userInfo.getDireccionContacto() : "")
            .telefono(Objects.nonNull(userInfo.getTelefono()) ? userInfo.getTelefono() : "")
            .emailContacto(Objects.nonNull(userInfo.getEmailContacto()) ? userInfo.getEmailContacto() : "")
            .localidad(LocalidadDTO.toDto(userInfo.getLocalidad()))
            .build()
        : emptyDTO();
    }

    public static UserInfoDTO emptyDTO() {
        return UserInfoDTO.builder()
                .dni("")
                .legajo("")
                .direccionContacto("")
                .telefono("")
                .emailContacto("")
                .localidad(LocalidadDTO.emptyDTO())
                .build();
    }

}