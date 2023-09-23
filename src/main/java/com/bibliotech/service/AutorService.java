package com.bibliotech.service;

import com.bibliotech.entity.Autor;
import java.util.List;
import java.util.Optional;

public interface AutorService {
    List<Autor> findAll();
    Autor save(Autor autor);

    Autor edit(Autor autor, Long id);

    Optional<Autor> delete(Long id);
}
