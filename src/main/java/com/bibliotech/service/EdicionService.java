package com.bibliotech.service;

import com.bibliotech.entity.Edicion;
import java.util.List;
import java.util.Optional;

public interface EdicionService {
    List<Edicion> findAll();

    Edicion save(Edicion edicion);

    Edicion edit(Edicion edicion, Long id);

    Optional<Edicion> delete(Long id);
}
