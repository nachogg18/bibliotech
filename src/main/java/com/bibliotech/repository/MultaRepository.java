package com.bibliotech.repository;

import com.bibliotech.entity.Multa;
import com.bibliotech.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long>, JpaSpecificationExecutor<Multa> {
    List<Multa> findMultasByUsuarioId(Long usuario_id);
}
