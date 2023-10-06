package com.bibliotech.repository;

import com.bibliotech.entity.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {
    Optional<Multa> findByIdAndUserId(Long id, Long userId);
}
