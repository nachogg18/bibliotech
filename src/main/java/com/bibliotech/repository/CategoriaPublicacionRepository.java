package com.bibliotech.repository;

import com.bibliotech.entity.Autor;
import com.bibliotech.entity.CategoriaPublicacion;
import java.util.Collection;
import java.util.List;

public interface CategoriaPublicacionRepository extends BaseRepository<CategoriaPublicacion, Long> {
    List<CategoriaPublicacion> findByCategoriaValorListIdIn(Collection<Long> categoriaValorList_id);
    List<CategoriaPublicacion> findByIdIn(Long[] ids);
}
