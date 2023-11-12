package com.bibliotech.security.dao.response;

import com.bibliotech.dto.CarreraDTO;
import com.bibliotech.dto.FacultadDTO;
import com.bibliotech.dto.LocalidadDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserInfoResponse {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleDto> roles;
    private String legajo;
    private String dni;
    private String telefono;
    private String direccion;
    private LocalidadDTO localidad;
    private FacultadDTO facultad;
    private CarreraDTO carrera;
}