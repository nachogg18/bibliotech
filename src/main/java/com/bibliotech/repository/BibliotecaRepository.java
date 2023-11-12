package com.bibliotech.repository;

import com.bibliotech.entity.Biblioteca;
import org.springframework.stereotype.Repository;

@Repository
public interface BibliotecaRepository extends BaseRepository<Biblioteca, Long> {
    Biblioteca findOneById(Long id);
}
