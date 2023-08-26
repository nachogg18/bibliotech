package com.bibliotech.repository;

import com.bibliotech.entity.Edicion;
import com.bibliotech.entity.Editorial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditorialRepository extends BaseRepository<Edicion, Long> {
    List<Editorial> findByNombreContainingIgnoreCase(String nombre);
}
