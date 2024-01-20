package com.bibliotech.service;

import com.bibliotech.dto.BibliotecaCreateDTO;
import com.bibliotech.dto.BibliotecaDetalleDTO;
import com.bibliotech.dto.UbicacionResponseDTO;
import com.bibliotech.entity.Biblioteca;

import java.util.List;

public interface BibliotecaService {

    Biblioteca findOne(Long id);

    Biblioteca delete(Long id);
    
    List<Biblioteca> findAll();

    Biblioteca saveBiblioteca(Biblioteca biblioteca);

    BibliotecaDetalleDTO crearBiblioteca(BibliotecaCreateDTO biblioteca);
    
    Biblioteca save(Biblioteca biblioteca);

    BibliotecaDetalleDTO edit(Long id, BibliotecaCreateDTO biblioteca);

    BibliotecaDetalleDTO delete(Long id);

    BibliotecaDetalleDTO findBibliotecaDetalle(Long id);

    List<UbicacionResponseDTO> findBibliotecaUbicaciones(Long id);


}
