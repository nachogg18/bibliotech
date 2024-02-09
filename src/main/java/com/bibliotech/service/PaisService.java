package com.bibliotech.service;

import com.bibliotech.entity.Pais;
import java.util.List;
import java.util.Optional;

public interface PaisService {
    Optional<Pais> findByIdAndFechaBajaNull(Long id);

    List<Pais> findByFechaBajaNull();
    
    Pais save(Pais pais);

    Long count();
}
