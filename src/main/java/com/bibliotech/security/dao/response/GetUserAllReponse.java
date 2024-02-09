package com.bibliotech.security.dao.response;

import com.bibliotech.dto.UserInfoDTO;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserAllReponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Instant deshailitado;
    private List<String> roles;
    private UserInfoDTO userInfoDTO;
}
