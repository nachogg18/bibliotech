package com.bibliotech.repository;

import com.bibliotech.entity.Provincia;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinciaRepository extends BaseRepository<Provincia, Long> {
    Optional<Provincia> findByIdAndAndFechaBajaNull(Long id);
    List<Provincia> findByFechaBajaNull();
}
