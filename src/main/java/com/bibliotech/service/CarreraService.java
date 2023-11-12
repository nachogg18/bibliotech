package com.bibliotech.service;

import com.bibliotech.entity.Carrera;
import java.util.List;
import java.util.Optional;

public interface CarreraService {

    List<Carrera> findAll();

    Optional<Carrera> findByIdAndFechaBajaNull(Long id);

    Carrera save(Carrera carrera);

    Carrera edit(Carrera carrera, Long id);

    Optional<Carrera> delete(Long id);
}
