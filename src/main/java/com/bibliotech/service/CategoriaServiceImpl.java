package com.bibliotech.service;

import com.bibliotech.dto.CrearCategoriaDTO;
import com.bibliotech.dto.FiltroCategoriaDTO;
import com.bibliotech.dto.MostrarCategoriaDTO;
import com.bibliotech.entity.Categoria;
import com.bibliotech.mapper.FiltroCategoriaDTOMapper;
import com.bibliotech.repository.CategoriaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, ModelMapper modelMapper) {
        this.categoriaRepository = categoriaRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findByFechaBajaNull();
    }

    @Override
    public Optional<Categoria> findOne(Long id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        Optional<Categoria> categoria = Optional.empty();
        if (categoriaOptional.isPresent())
            categoria = categoriaOptional;
        return categoria;
    }


    @Override
    public MostrarCategoriaDTO save(CrearCategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.save(modelMapper.map(categoriaDTO, Categoria.class));
        return modelMapper.map(categoria, MostrarCategoriaDTO.class);
    }

    @Override
    public Categoria edit(Categoria categoria, Long id) {
        if (categoriaRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return categoriaRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> delete(Long id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isPresent()) {
            Categoria categoria = categoriaOptional.get();
            if (categoria.getFechaBaja() != null)
                categoriaOptional = Optional.empty();
            else {
                categoria.setFechaBaja(Instant.now());
                categoriaOptional = Optional.of(categoriaRepository.save(categoria));
            }
        }
        return categoriaOptional;
    }

    @Override
    public List<FiltroCategoriaDTO> findAllDTO() {
        return FiltroCategoriaDTOMapper.toCategoriaDTO(categoriaRepository.findAll());
    }
}
