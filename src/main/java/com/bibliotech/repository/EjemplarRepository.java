package com.bibliotech.repository;

import com.bibliotech.entity.Ejemplar;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EjemplarRepository extends BaseRepository<Ejemplar, Long> {
    Optional<Ejemplar> findById(Long id);
    List<Ejemplar> findByFechaBajaNull();

}
