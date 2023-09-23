package com.bibliotech.service;

import com.bibliotech.dto.CrearEjemplarDTO;
import com.bibliotech.entity.Ejemplar;
import java.util.List;

public interface EjemplarService {
    List<Ejemplar> findAll();

    Ejemplar createEjemplar(CrearEjemplarDTO request) throws Exception;

    Ejemplar save(Ejemplar ejemplar);

    Ejemplar edit(Ejemplar ejemplar, Long id);

    void delete(Long id);
}
