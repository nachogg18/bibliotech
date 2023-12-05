package com.bibliotech.service;

import com.bibliotech.entity.Biblioteca;
import com.bibliotech.repository.BibliotecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BibliotecaServiceImpl implements BibliotecaService {

    private final BibliotecaRepository bibliotecaRepository;

    @Override
    public Biblioteca findOne(Long id){
        Biblioteca biblioteca;
        try {
            biblioteca = bibliotecaRepository.findOneById(id);
        } catch (Exception e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bilbioteca no entontrada");
        }
        return biblioteca;
    }

    @Override
    public List<Biblioteca> findAll() {
        return bibliotecaRepository.findAll();
    }

    @Override
    public Biblioteca save(Biblioteca biblioteca) {
        return bibliotecaRepository.save(biblioteca);
    }
}
