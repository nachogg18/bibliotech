package com.bibliotech.service;

import com.bibliotech.entity.CategoriaValor;
import com.bibliotech.repository.CategoriaValorRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoriaValorServiceImpl implements CategoriaValorService {

    private final CategoriaValorRepository categoriaValorRepository;

    public CategoriaValorServiceImpl(CategoriaValorRepository categoriaValorRepository) {
        this.categoriaValorRepository = categoriaValorRepository;
    }

    @Override
    public List<CategoriaValor> findAll() {
        return categoriaValorRepository.findByFechaBajaNull();
    }


    @Override
    public CategoriaValor save(CategoriaValor categoriaValor) {
        return categoriaValorRepository.save(categoriaValor);
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
        if(categoriaValorOptional.isPresent()) {
            CategoriaValor categoriaValor = categoriaValorOptional.get();
            if(categoriaValor.getFechaBaja() != null)
                categoriaValorOptional = Optional.empty();
            else {
                categoriaValor.setFechaBaja(Instant.now());
                categoriaValorOptional = Optional.of(categoriaValorRepository.save(categoriaValor));
            }
        }
        return categoriaValorOptional;
    }
}
