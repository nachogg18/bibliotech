package com.bibliotech.service;

import com.bibliotech.entity.Ejemplar;
import com.bibliotech.dto.CrearEjemplarDTO;

import java.util.List;
import java.util.Optional;

public interface EjemplarService {
    List<Ejemplar> findAll();

    Ejemplar createEjemplar(CrearEjemplarDTO request) throws Exception;

    Ejemplar save(Ejemplar ejemplar);

    Ejemplar edit(Ejemplar ejemplar, Long id);

    void delete(Long id);
    Optional<Ejemplar> findById(Long id);
}
