package com.bibliotech.dto;

import com.bibliotech.security.entity.UserInfo;
import java.util.Objects;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FindUserInfoDTO {
    private String legajo;
    private String dni;
    private String telefono;
    private String emailContacto;

    public static FindUserInfoDTO toDto(UserInfo userInfo) {
        return Objects.nonNull(userInfo) ? FindUserInfoDTO.builder()
                .legajo(Objects.nonNull(userInfo.getLegajo()) ? userInfo.getLegajo() : "")
                .dni(Objects.nonNull(userInfo.getDni()) ? userInfo.getDni() : "")
                .telefono(Objects.nonNull(userInfo.getTelefono()) ? userInfo.getTelefono() : "")
                .emailContacto(Objects.nonNull(userInfo.getEmailContacto()) ? userInfo.getEmailContacto() : "")
                .build() : emptyDTO();
    }

    public static FindUserInfoDTO emptyDTO() {
        return FindUserInfoDTO.builder()
                .legajo("")
                .dni("")
                .telefono("")
                .emailContacto("")
                .build();
    }

}