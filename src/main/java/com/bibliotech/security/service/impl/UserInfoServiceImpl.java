package com.bibliotech.security.service.impl;

import com.bibliotech.security.entity.UserInfo;
import com.bibliotech.security.repository.UserInfoRepository;
import com.bibliotech.security.service.UserInfoService;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

  private final UserInfoRepository userInfoRepository;

  @Override
  public UserInfo saveUserInfo(UserInfo userInfo) {
    return userInfoRepository.save(userInfo);
  }

  @Override
  public Optional<UserInfo> getById(Long id) {
    return userInfoRepository.findById(id);
  }
}