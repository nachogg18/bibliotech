package com.bibliotech.repository;

import com.bibliotech.entity.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {
    List<Facultad> findByFechaBajaNull();
    Optional<Facultad> findByIdAndAndFechaBajaNull(Long id);
}
