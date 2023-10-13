package com.bibliotech.service;

import com.bibliotech.entity.Biblioteca;
import com.bibliotech.repository.BibliotecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BibliotecaServiceImpl implements BibliotecaService {
    private final BibliotecaRepository bibliotecaRepository;


    @Override
    public List<Biblioteca> findAll() {
        return bibliotecaRepository.findAll();
    }

    @Override
    public Biblioteca save(Biblioteca biblioteca) {
        return bibliotecaRepository.save(biblioteca);
    }
}
