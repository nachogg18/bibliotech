package com.bibliotech.service;

import com.bibliotech.entity.Provincia;
import com.bibliotech.repository.ProvinciaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProvinciaServiceImpl implements ProvinciaService {

    private final ProvinciaRepository provinciaRepository;

    @Override
    public Optional<Provincia> findByIdAndFechaBajaNull(Long id) {
        return provinciaRepository.findByIdAndAndFechaBajaNull(id);
    }

    @Override
    public List<Provincia> findByFechaBajaNull() {
        return provinciaRepository.findByFechaBajaNull();
    }

    @Override
    public Provincia save(Provincia provincia) {
        return provinciaRepository.save(provincia);
    }
}
