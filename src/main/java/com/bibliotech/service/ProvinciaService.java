package com.bibliotech.service;

import com.bibliotech.dto.CrearProvinciaDTO;
import com.bibliotech.entity.Provincia;
import java.util.List;
import java.util.Optional;

public interface ProvinciaService {
    Optional<Provincia> findByIdAndFechaBajaNull(Long id);

    List<Provincia> findByFechaBajaNull();

    Provincia crearProvincia(CrearProvinciaDTO provinciaDTO);
    Provincia save(Provincia provincia);
}
