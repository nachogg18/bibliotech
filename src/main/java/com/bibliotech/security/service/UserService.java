package com.bibliotech.security.service;

import com.bibliotech.security.dao.request.EditUserRequest;
import com.bibliotech.security.dao.response.FindUserDto;
import com.bibliotech.security.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();
    Optional<User> findById(Long id);
    List<User> findAll();
    List<FindUserDto> getUsers();
    User save(User user);
    User edit(Long UserId, EditUserRequest request);
    boolean exists(Long id);
}