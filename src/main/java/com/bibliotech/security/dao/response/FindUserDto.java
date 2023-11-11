package com.bibliotech.security.dao.response;

import com.bibliotech.dto.FindUserInfoDTO;
import com.bibliotech.security.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindUserDto {
    private Long id;
    private FindUserInfoDTO userInfo;


    public static FindUserDto toDto(User user) {
        return FindUserDto.builder()
                .id(user.getId())
                .userInfo(FindUserInfoDTO.toDto(user.getUserInfo()))
                .build();
    }
}
