package com.bibliotech.repository;

import com.bibliotech.entity.TipoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPublicacionRepository extends JpaRepository<TipoPublicacion, Long> {
}
