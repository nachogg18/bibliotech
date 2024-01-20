package com.bibliotech.service;

import com.bibliotech.dto.BIbliotecaCreateUpdateDTO;
import com.bibliotech.dto.BibliotecaDetalleDTO;
import com.bibliotech.dto.UbicacionResponseDTO;
import com.bibliotech.entity.Biblioteca;

import java.util.List;

public interface BibliotecaService {

    Biblioteca findOne(Long id);
    Biblioteca delete(Long id);
    List<Biblioteca> findAll();
    Biblioteca save(BIbliotecaCreateUpdateDTO biblioteca);

    Biblioteca saveBiblioteca(Biblioteca biblioteca);
    Biblioteca update(Long id, BIbliotecaCreateUpdateDTO biblioteca);
    BibliotecaDetalleDTO findBibliotecaDetalle(Long id);
    List<UbicacionResponseDTO> findBibliotecaUbicaciones(Long id);


}
