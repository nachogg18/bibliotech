package com.bibliotech.service;

import com.bibliotech.entity.Localidad;

import java.util.List;
import java.util.Optional;

public interface LocalidadService {
    Optional<Localidad> findByIdAndFechaBajaNull(Long id);

    List<Localidad> findByFechaBajaNull();

    Localidad save(Localidad localidad);
}
