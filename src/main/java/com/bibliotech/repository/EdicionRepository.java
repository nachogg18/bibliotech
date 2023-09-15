package com.bibliotech.repository;

import com.bibliotech.entity.Edicion;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EdicionRepository extends BaseRepository<Edicion, Long> {
    List<Edicion> findByFechaBajaNull();
}
