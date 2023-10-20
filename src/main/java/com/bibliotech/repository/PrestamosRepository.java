package com.bibliotech.repository;

import com.bibliotech.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamosRepository extends BaseRepository<Prestamo, Long>, JpaSpecificationExecutor<Prestamo> {
    List<Prestamo> findPrestamoByUsuarioId(Long usuario_id);
}
