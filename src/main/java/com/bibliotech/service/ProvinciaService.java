package com.bibliotech.service;

import com.bibliotech.entity.Provincia;
import java.util.List;
import java.util.Optional;

public interface ProvinciaService {
    Optional<Provincia> findByIdAndFechaBajaNull(Long id);

    List<Provincia> findByFechaBajaNull();
    
    Provincia save(Provincia provincia);
}
