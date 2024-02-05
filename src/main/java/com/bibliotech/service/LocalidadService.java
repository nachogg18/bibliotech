package com.bibliotech.service;

import com.bibliotech.dto.CrearLocalidadDTO;
import com.bibliotech.dto.LocalidadDTO;
import com.bibliotech.entity.Localidad;

import java.util.List;
import java.util.Optional;

public interface LocalidadService {
    Optional<Localidad> findByIdAndFechaBajaNull(Long id);

    List<Localidad> findByFechaBajaNull();
    Localidad crearLocalidad(CrearLocalidadDTO localidadDTO);

    List<LocalidadDTO> findByProvinciaId(Long id);

    Localidad save(Localidad localidad);

    Long count();
}
