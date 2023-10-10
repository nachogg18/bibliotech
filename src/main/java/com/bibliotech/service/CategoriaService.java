package com.bibliotech.service;

import com.bibliotech.dto.CrearCategoriaDTO;
import com.bibliotech.dto.FiltroCategoriaDTO;
import com.bibliotech.dto.MostrarCategoriaDTO;
import com.bibliotech.dto.MostrarCategoriaValorDTO;
import com.bibliotech.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<MostrarCategoriaDTO> findAll();
    Optional<Categoria> findOne(Long id);

    MostrarCategoriaDTO save(CrearCategoriaDTO categoriaDTO);

    Categoria save(Categoria categoria);

    MostrarCategoriaValorDTO edit(Categoria categoria, Long id);

    Optional<Categoria> delete(Long id);

    List<FiltroCategoriaDTO> findAllDTO();
}
