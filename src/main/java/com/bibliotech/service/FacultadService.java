package com.bibliotech.service;

import com.bibliotech.entity.Facultad;

import java.util.List;
import java.util.Optional;

public interface FacultadService {
    List<Facultad> findAll();

    Facultad save(Facultad facultad);

    Facultad edit(Facultad facultad, Long id);

    Optional<Facultad> delete(Long id);
}
