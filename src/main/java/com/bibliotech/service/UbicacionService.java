package com.bibliotech.service;

import com.bibliotech.dto.UbicacionDTO;
import com.bibliotech.entity.Ubicacion;

import java.util.List;
import java.util.Optional;


public interface UbicacionService {
    List<Ubicacion> findAll();

    List<Ubicacion> findAllDisponibles();

    public List<Ubicacion> findAllDispoblesWith(Long id);

    Optional<Ubicacion> findById(Long id);

    Ubicacion save(UbicacionDTO ubicacion);

    Ubicacion edit(Ubicacion ubicacion, Long id);

    Ubicacion changeOcupada(Long id, boolean state);

    Optional<Ubicacion> delete(Long id);

    Ubicacion saveChanges(Ubicacion ubicacion);
}
