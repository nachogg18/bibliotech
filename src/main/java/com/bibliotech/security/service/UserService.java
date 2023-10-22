package com.bibliotech.security.service;

import com.bibliotech.security.dao.request.EditUserRequest;
import com.bibliotech.security.dao.response.FindUserDto;
import com.bibliotech.security.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
    List<User> findAll();
    List<FindUserDto> getUsers();
    User save(User user);
    User edit(Long UserId, EditUserRequest request);
    Optional<User> getActiveUserByEmail(String email);

    Optional<User> getUserToConfirmByEmail(String email);
}