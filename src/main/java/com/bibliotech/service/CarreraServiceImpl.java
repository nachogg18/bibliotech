package com.bibliotech.service;

import com.bibliotech.entity.Carrera;
import com.bibliotech.entity.Edicion;
import com.bibliotech.repository.CarreraRepository;
import com.bibliotech.repository.EdicionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;

    @Override
    public List<Carrera> findAll() {
        return carreraRepository.findByFechaBajaNull();
    }

    @Override
    public Optional<Carrera> findById(Long id) {
        return carreraRepository.findById(id);
    }


    @Override
    public Carrera save(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    @Override
    public Carrera edit(Carrera carrera, Long id) {
        if (carreraRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        carrera.setId(id);
        return carreraRepository.save(carrera);
    }

    @Override
    public Optional<Carrera> delete(Long id) {
        Optional<Carrera> carreraOptional = carreraRepository.findById(id);
        if(carreraOptional.isPresent()) {
            Carrera carrera = carreraOptional.get();
            if(carrera.getFechaBaja() != null)
                carreraOptional = Optional.empty();
            else {
                carrera.setFechaBaja(Instant.now());
                carrera.setId(id);
                carreraOptional = Optional.of(carreraRepository.save(carrera));
            }
        }
        return carreraOptional;
    }
}