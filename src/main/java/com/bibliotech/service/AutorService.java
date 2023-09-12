package com.bibliotech.service;

import com.bibliotech.entity.Autor;

import java.util.List;

public interface AutorService {
    List<Autor> findAll();
    Autor save(Autor autor);

    Autor edit(Autor autor, Long id);

    void delete(Long id);
}
