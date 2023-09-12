package com.bibliotech.service;

import com.bibliotech.entity.Edicion;

import java.util.List;

public interface EdicionService {
    List<Edicion> findAll();

    Edicion save(Edicion edicion);

    Edicion edit(Edicion edicion, Long id);

    void delete(Long id);
}
