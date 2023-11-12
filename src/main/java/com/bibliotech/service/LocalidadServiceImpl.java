package com.bibliotech.service;

import com.bibliotech.entity.Localidad;
import com.bibliotech.repository.LocalidadRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LocalidadServiceImpl implements LocalidadService {

    private final LocalidadRepository localidadRepository;

    @Override
    public Optional<Localidad> findByIdAndFechaBajaNull(Long id) {
        return localidadRepository.findByIdAndAndFechaBajaNull(id);
    }

    @Override
    public List<Localidad> findByFechaBajaNull() {
        return localidadRepository.findByFechaBajaNull();
    }

    @Override
    public Localidad save(Localidad localidad) {
        return localidadRepository.save(localidad);
    }
}
