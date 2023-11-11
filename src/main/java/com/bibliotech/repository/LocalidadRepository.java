package com.bibliotech.repository;

import com.bibliotech.entity.Localidad;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalidadRepository extends BaseRepository<Localidad, Long> {
    Optional<Localidad> findByIdAndAndFechaBajaNull(Long id);
    List<Localidad> findByFechaBajaNull();
}
