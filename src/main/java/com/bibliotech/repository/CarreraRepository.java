package com.bibliotech.repository;

import com.bibliotech.entity.Carrera;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraRepository extends BaseRepository<Carrera, Long> {
    List<Carrera> findByFechaBajaNull();

    Optional<Carrera> findByIdAndFechaBajaNull(Long id);
}