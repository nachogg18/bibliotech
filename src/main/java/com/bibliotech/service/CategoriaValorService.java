package com.bibliotech.service;

import com.bibliotech.entity.CategoriaValor;
import java.util.List;
import java.util.Optional;

public interface CategoriaValorService {
    List<CategoriaValor> findAll();

    CategoriaValor save(CategoriaValor categoriaValor);

    CategoriaValor edit(CategoriaValor categoriaValor, Long id);

    Optional<CategoriaValor> delete(Long id);
}
