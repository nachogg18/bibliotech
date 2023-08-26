package com.bibliotech.service;

import com.bibliotech.dto.FiltroCategoriaDTO;

import java.util.List;

public interface CategoriaService {
    List<FiltroCategoriaDTO> findAllDTO();
}
