package com.bibliotech.service;

import com.bibliotech.entity.Ubicacion;

import java.util.List;
import java.util.Optional;


public interface UbicacionService {
    List<Ubicacion> findAll();

    List<Ubicacion> findAllDispobles();

    public List<Ubicacion> findAllDispoblesWith(Long id);

    Optional<Ubicacion> findById(Long id);

    Ubicacion save(Ubicacion ubicacion);

    Ubicacion edit(Ubicacion ubicacion, Long id);

    Ubicacion changeOcupada(Long id, boolean state);

    Optional<Ubicacion> delete(Long id);
}
