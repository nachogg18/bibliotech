package com.bibliotech.service;

import com.bibliotech.dto.BibliotecaDetalleDTO;
import com.bibliotech.dto.UbicacionResponseDTO;
import com.bibliotech.entity.Biblioteca;

import java.util.List;

public interface BibliotecaService {

    Biblioteca findOne(Long id);
    List<Biblioteca> findAll();
    Biblioteca save(Biblioteca biblioteca);

    BibliotecaDetalleDTO findBibliotecaDetalle(Long id);

    List<UbicacionResponseDTO> findBibliotecaUbicaciones(Long id);
}
