package com.bibliotech.repository;

import com.bibliotech.entity.Edicion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdicionRepository extends BaseRepository<Edicion, Long> {
    List<Edicion> findByFechaBajaNull();
}
