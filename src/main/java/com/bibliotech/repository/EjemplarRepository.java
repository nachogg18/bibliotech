package com.bibliotech.repository;

import com.bibliotech.entity.Ejemplar;
import org.springframework.stereotype.Repository;

@Repository
public interface EjemplarRepository extends BaseRepository<Ejemplar, Long> {
}
