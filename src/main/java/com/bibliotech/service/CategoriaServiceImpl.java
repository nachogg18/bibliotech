package com.bibliotech.service;

import com.bibliotech.dto.CrearCategoriaDTO;
import com.bibliotech.dto.FiltroCategoriaDTO;
import com.bibliotech.dto.MostrarCategoriaDTO;
import com.bibliotech.dto.MostrarCategoriaValorDTO;
import com.bibliotech.entity.Categoria;
import com.bibliotech.mapper.FiltroCategoriaDTOMapper;
import com.bibliotech.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<MostrarCategoriaDTO> findAll() {
        return categoriaRepository.findByFechaBajaNull()
                .stream().map(c -> modelMapper.map(c, MostrarCategoriaDTO.class)).toList();
    }

    @Override
    public Optional<Categoria> findOne(Long id) {
        Optional<Categoria> categoriaOption = categoriaRepository.findById(id);
        Optional<Categoria> categoria = Optional.empty();
        if (categoriaOption.isPresent())
            categoria = categoriaOption;
        return categoria;
    }


    @Override
    public MostrarCategoriaDTO save(CrearCategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaRepository.save(modelMapper.map(categoriaDTO, Categoria.class));
        log.info(categoria);
        return modelMapper.map(categoria, MostrarCategoriaDTO.class);
    }

    @Override
    public MostrarCategoriaValorDTO edit(Categoria categoria, Long id) {
        if (categoriaRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        categoria.setId(id);
        categoria = categoriaRepository.save(categoria);
        return modelMapper.map(categoria, MostrarCategoriaValorDTO.class);
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
                categoria.setId(id);
                categoriaOptional = Optional.of(categoriaRepository.save(categoria));
            }
        }
        return categoriaOptional;
    }

    @Override
    public List<FiltroCategoriaDTO> findAllDTO() {
        return FiltroCategoriaDTOMapper.toCategoriaDTO(categoriaRepository.findAll());
    }

    @Override
    public Long count() {
        return categoriaRepository.count();
    }
}
