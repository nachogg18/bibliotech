package com.bibliotech.security.repository;

import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Since email is unique, we'll find users by email
    Optional<User> findByEmail(String email);
    List<User> findByRolesContains(Role role);
}