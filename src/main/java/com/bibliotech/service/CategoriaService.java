package com.bibliotech.service;

import com.bibliotech.dto.FiltroCategoriaDTO;
import com.bibliotech.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> findAll();

    Categoria save(Categoria categoria);

    Categoria edit(Categoria categoria, Long id);

    Optional<Categoria> delete(Long id);

    List<FiltroCategoriaDTO> findAllDTO();
}
