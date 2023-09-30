package com.bibliotech.repository;

import com.bibliotech.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamosRepository extends BaseRepository<Prestamo, Long>, JpaSpecificationExecutor<Prestamo> {
}
