package com.bibliotech.service;

import com.bibliotech.entity.TipoMulta;

import java.util.List;
import java.util.Optional;

public interface TipoMultaService {
    List<TipoMulta> findAll();

    Optional<TipoMulta> findById(Long id);

    TipoMulta save(TipoMulta tipoMulta);

    TipoMulta edit(TipoMulta tipoMulta, Long id);

    Optional<TipoMulta> delete(Long id);

    TipoMulta findByNombre(String nombre);
}
