package com.bibliotech.repository;

import com.bibliotech.entity.Pais;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PaisRepository extends BaseRepository<Pais, Long> {
    Optional<Pais> findByIdAndAndFechaBajaNull(Long id);
    List<Pais> findByFechaBajaNull();
}
