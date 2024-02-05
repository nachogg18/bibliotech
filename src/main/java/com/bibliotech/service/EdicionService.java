package com.bibliotech.service;

import com.bibliotech.entity.Edicion;
import java.util.List;
import java.util.Optional;

public interface EdicionService {
    List<Edicion> findAll();

    Optional<Edicion> findById(Long id);

    Edicion save(Edicion edicion);

    Edicion edit(Edicion edicion, Long id);

    Optional<Edicion> delete(Long id);

    Long count();
}
