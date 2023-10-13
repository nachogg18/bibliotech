package com.bibliotech.security.service;

import com.bibliotech.security.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDetailsService userDetailsService();
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);

    boolean exists(Long id);
}