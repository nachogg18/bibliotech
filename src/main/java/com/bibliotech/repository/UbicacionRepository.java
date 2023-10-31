package com.bibliotech.repository;

import com.bibliotech.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByFechaBajaNull();

    List<Ubicacion> findByFechaBajaNullAndOcupadaFalse();
}
