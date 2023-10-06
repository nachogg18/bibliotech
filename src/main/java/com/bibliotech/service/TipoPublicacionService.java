package com.bibliotech.service;

import com.bibliotech.entity.TipoPublicacion;
import java.util.List;
import java.util.Optional;

public interface TipoPublicacionService {
    List<TipoPublicacion> findAll();

    TipoPublicacion save(TipoPublicacion tipoPublicacion);

    TipoPublicacion edit(TipoPublicacion tipoPublicacion, Long id);

    Optional<TipoPublicacion> delete(Long id);

    Optional<TipoPublicacion> findByIdAndFechaBajaNull(Long id);
}