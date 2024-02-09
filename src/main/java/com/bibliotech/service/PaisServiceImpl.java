package com.bibliotech.service;

import com.bibliotech.entity.Pais;
import com.bibliotech.repository.PaisRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PaisServiceImpl implements PaisService {

    private final PaisRepository paisRepository;

    @Override
    public Optional<Pais> findByIdAndFechaBajaNull(Long id) {
        return paisRepository.findByIdAndAndFechaBajaNull(id);
    }

    @Override
    public List<Pais> findByFechaBajaNull() {
        return paisRepository.findByFechaBajaNull();
    }

    @Override
    public Pais save(Pais pais) {
        return paisRepository.save(pais);
    }

    @Override
    public Long count() {
        return paisRepository.count();
    }
}
