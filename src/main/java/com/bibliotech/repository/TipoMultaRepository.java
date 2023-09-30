package com.bibliotech.repository;

import com.bibliotech.entity.TipoMulta;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMultaRepository extends BaseRepository<TipoMulta, Long>{
    List<TipoMulta> findByFechaBajaNull();
}
