package com.bibliotech.service;

import com.bibliotech.entity.Autor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SeedService {

    @Transactional
    String cargarAutores();
    @Transactional
    String cargarBilbiotecas();
    @Transactional
    String cargarCarreras();
    @Transactional
    String cargarCategorias();
    @Transactional
    String cargarValores();
    @Transactional
    String cargarEdiciones();
    @Transactional
    String cargarEditoriales();
    @Transactional
    String cargarFacultades();
    @Transactional
    String cargarPlataformas();
    @Transactional
    String cargarTiposPublicacion();
    @Transactional
    String cargarTiposMulta();
    @Transactional
    String cargarUbicaciones();
    @Transactional
    String cargarPublicaciones();
    @Transactional
    String cargarEjemplares();
    @Transactional
    String cargarUsuarios();
    @Transactional
    String cargarParametros();
    @Transactional
    String cargaMasiva();
}
