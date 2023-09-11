package com.bibliotech.service;

import com.bibliotech.entity.Autor;
import com.bibliotech.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    public AutorServiceImpl(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public List<Autor> findAll() {
        return autorRepository.findAll();
    }
}
