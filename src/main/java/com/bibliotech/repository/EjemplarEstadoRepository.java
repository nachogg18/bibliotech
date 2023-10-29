package com.bibliotech.repository;

import com.bibliotech.entity.EjemplarEstado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjemplarEstadoRepository extends CrudRepository<EjemplarEstado, Long> {
}
