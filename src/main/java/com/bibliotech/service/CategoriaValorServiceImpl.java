package com.bibliotech.service;

import com.bibliotech.dto.CrearValorDTO;
import com.bibliotech.dto.MostrarCategoriaDTO;
import com.bibliotech.dto.MostrarCategoriaValorDTO;
import com.bibliotech.entity.Categoria;
import com.bibliotech.entity.CategoriaValor;
import com.bibliotech.repository.CategoriaValorRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Log4j2
public class CategoriaValorServiceImpl implements CategoriaValorService {

    private final CategoriaValorRepository categoriaValorRepository;
    private final CategoriaService categoriaService;
    private final ModelMapper modelMapper;

    public CategoriaValorServiceImpl(CategoriaValorRepository categoriaValorRepository, CategoriaService categoriaService, ModelMapper modelMapper) {
        this.categoriaValorRepository = categoriaValorRepository;
        this.categoriaService = categoriaService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoriaValor> findAll() {
        return categoriaValorRepository.findByFechaBajaNull();
    }

    @Override
    public MostrarCategoriaValorDTO save(CrearValorDTO valorDTO) {
        Categoria categoria = categoriaService.findOne(valorDTO.getIdCategoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
        CategoriaValor valor = new CategoriaValor();
        valor.setNombre(valorDTO.getNombre());
        valor.setCategoria(categoria);
        valor = categoriaValorRepository.save(valor);
        MostrarCategoriaValorDTO retorna = modelMapper.map(valor, MostrarCategoriaValorDTO.class);
        log.info(valorDTO.toString());
        log.info(categoria.toString());
        log.info(valor.toString());
        log.info(retorna.toString());
        return retorna;
    }

    @Override
    public CategoriaValor edit(CategoriaValor categoriaValor, Long id) {
        if (categoriaValorRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return categoriaValorRepository.save(categoriaValor);
    }

    @Override
    public Optional<CategoriaValor> delete(Long id) {
        Optional<CategoriaValor> categoriaValorOptional = categoriaValorRepository.findById(id);
        if (categoriaValorOptional.isPresent()) {
            CategoriaValor categoriaValor = categoriaValorOptional.get();
            if (categoriaValor.getFechaBaja() != null)
                categoriaValorOptional = Optional.empty();
            else {
                categoriaValor.setFechaBaja(Instant.now());
                categoriaValorOptional = Optional.of(categoriaValorRepository.save(categoriaValor));
            }
        }
        return categoriaValorOptional;
    }
}
