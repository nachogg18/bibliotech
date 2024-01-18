package com.bibliotech.security.repository;

import com.bibliotech.security.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByName(String name);

    Optional<Privilege> findByIdAndEndDateNull(Long id);

    List<Privilege> findByEndDateNull();
}
