package com.bibliotech.security.service;


import com.bibliotech.security.entity.UserInfo;
import java.util.Optional;

public interface UserInfoService {
    UserInfo saveUserInfo(UserInfo userInfo);
    Optional<UserInfo> getById(Long id);
}