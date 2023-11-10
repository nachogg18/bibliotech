package com.bibliotech.service;

import com.bibliotech.entity.Biblioteca;

import java.util.List;

public interface BibliotecaService {

    Biblioteca findOne(Long id);
    List<Biblioteca> findAll();
    Biblioteca save(Biblioteca biblioteca);
}
