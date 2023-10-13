package com.bibliotech.service;

import com.bibliotech.entity.Ubicacion;

import java.util.List;
import java.util.Optional;

public interface UbicacionService {
    List<Ubicacion> findAll();

    Optional<Ubicacion> findById(Long id);

    Ubicacion save(Ubicacion ubicacion);

    Ubicacion edit(Ubicacion ubicacion, Long id);

    Optional<Ubicacion> delete(Long id);
}
