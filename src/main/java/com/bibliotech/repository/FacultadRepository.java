package com.bibliotech.repository;

import com.bibliotech.entity.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {
    List<Facultad> findByFechaBajaNull();
}
