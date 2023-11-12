package com.bibliotech.service;

import com.bibliotech.entity.Carrera;
import com.bibliotech.entity.Editorial;

import java.util.List;
import java.util.Optional;

public interface CarreraService {

    List<Carrera> findAll();

    Optional<Carrera> findById(Long id);

    Carrera save(Carrera carrera);

    Carrera edit(Carrera carrera, Long id);

    Optional<Carrera> delete(Long id);
}
