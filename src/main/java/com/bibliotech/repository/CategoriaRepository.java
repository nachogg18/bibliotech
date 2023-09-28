package com.bibliotech.repository;


import com.bibliotech.entity.Categoria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends BaseRepository<Categoria, Long> {
    List<Categoria> findByFechaBajaNull();
}
