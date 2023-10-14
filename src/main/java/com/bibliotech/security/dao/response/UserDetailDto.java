package com.bibliotech.security.dao.response;

import java.util.List;
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
}