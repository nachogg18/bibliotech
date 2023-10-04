package com.bibliotech.service;

import com.bibliotech.dto.CrearValorDTO;
import com.bibliotech.dto.MostrarCategoriaValorDTO;
import com.bibliotech.entity.CategoriaValor;
import java.util.List;
import java.util.Optional;

public interface CategoriaValorService {
    List<CategoriaValor> findAll();

    MostrarCategoriaValorDTO save(CrearValorDTO valorDTO);

    CategoriaValor edit(CategoriaValor categoriaValor, Long id);

    Optional<CategoriaValor> delete(Long id);
}
