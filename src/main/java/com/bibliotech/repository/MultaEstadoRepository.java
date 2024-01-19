package com.bibliotech.repository;

import com.bibliotech.entity.MultaEstado;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MultaEstadoRepository  extends BaseRepository<MultaEstado, Long>, JpaSpecificationExecutor<MultaEstado> {
}
