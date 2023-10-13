package com.bibliotech.service;

import com.bibliotech.entity.Plataforma;

import java.util.List;
import java.util.Optional;

public interface PlataformaService {
    List<Plataforma> findAll();

    Optional<Plataforma> findById(Long id);

    Plataforma save(Plataforma plataforma);

    Plataforma edit(Plataforma plataforma, Long id);

    void delete(Long id);
}
