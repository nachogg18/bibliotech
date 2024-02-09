package com.bibliotech.service;

import com.bibliotech.dto.CrearValorDTO;
import com.bibliotech.dto.MostrarCategoriaValorDTO;
import com.bibliotech.entity.CategoriaValor;
import java.util.List;
import java.util.Optional;

public interface CategoriaValorService {
    List<CategoriaValor> findAll();

    Optional<CategoriaValor> findById(Long id);

    MostrarCategoriaValorDTO save(CrearValorDTO valorDTO);

    MostrarCategoriaValorDTO edit(CrearValorDTO valorDTO, Long id);

    Optional<MostrarCategoriaValorDTO> delete(Long id);

    Long count();
}
