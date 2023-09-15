package com.bibliotech.service;

import com.bibliotech.entity.Plataforma;
import java.util.List;

public interface PlataformaService {
    List<Plataforma> findAll();

    Plataforma save(Plataforma plataforma);

    Plataforma edit(Plataforma plataforma, Long id);

    void delete(Long id);
}
