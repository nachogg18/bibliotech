package com.bibliotech.security.repository;

import com.bibliotech.security.entity.Role;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    
    Optional<Role> findByNameAndEndDateNull(String name);

    Optional<Role> findByName(String name);

    Optional<Role> findByIdAndEndDateNull(Long id);

    List<Role> findByEndDateNull();
}