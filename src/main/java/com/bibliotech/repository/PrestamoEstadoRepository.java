package com.bibliotech.repository;

import com.bibliotech.entity.PrestamoEstado;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoEstadoRepository extends BaseRepository<PrestamoEstado, Long>, JpaSpecificationExecutor<PrestamoEstado> {

        }
