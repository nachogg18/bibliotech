package com.bibliotech.service;

import com.bibliotech.dto.FiltroCategoriaDTO;
import com.bibliotech.mapper.FiltroCategoriaDTOMapper;
import com.bibliotech.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<FiltroCategoriaDTO> findAllDTO() {
        return FiltroCategoriaDTOMapper.toCategoriaDTO(categoriaRepository.findAll());
    }
}
