package com.bibliotech.repository;

import com.bibliotech.entity.Carrera;
import com.bibliotech.entity.Edicion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarreraRepository extends BaseRepository<Carrera, Long> {
    List<Carrera> findByFechaBajaNull();
}