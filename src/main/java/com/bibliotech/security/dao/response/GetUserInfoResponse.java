package com.bibliotech.security.dao.response;

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
}