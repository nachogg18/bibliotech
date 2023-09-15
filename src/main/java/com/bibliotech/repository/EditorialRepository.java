package com.bibliotech.repository;

import com.bibliotech.entity.Editorial;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepository extends BaseRepository<Editorial, Long> {
    List<Editorial> findByFechaBajaNull();
    List<Editorial> findByNombreContainingIgnoreCase(String nombre);
}
