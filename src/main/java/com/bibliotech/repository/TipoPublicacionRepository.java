package com.bibliotech.repository;

import com.bibliotech.entity.TipoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoPublicacionRepository extends JpaRepository<TipoPublicacion, Long> {
    List<TipoPublicacion> findByFechaBajaNull();
    Optional<TipoPublicacion> findByIdAndFechaBajaNull(Long id);
}
