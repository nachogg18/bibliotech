package com.bibliotech.service;

import com.bibliotech.entity.Facultad;
import com.bibliotech.repository.FacultadRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FacultadServiceImpl implements FacultadService {

    private final FacultadRepository facultadRepository;

    @Override
    public List<Facultad> findAll() {
        return facultadRepository.findByFechaBajaNull();
    }


    @Override
    public Facultad save(Facultad facultad) {
        return facultadRepository.save(facultad);
    }

    @Override
    public Facultad edit(Facultad facultad, Long id) {
        if (facultadRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        facultad.setId(id);
        return facultadRepository.save(facultad);
    }

    @Override
    public Optional<Facultad> delete(Long id) {
        Optional<Facultad> facultadOptional = facultadRepository.findById(id);
        if (facultadOptional.isPresent()) {
            Facultad facultad = facultadOptional.get();
            if (facultad.getFechaBaja() != null)
                facultadOptional = Optional.empty();
            else {
                facultad.setId(id);
                facultad.setFechaBaja(Instant.now());
                facultadOptional = Optional.of(facultadRepository.save(facultad));
            }
        }
        return facultadOptional;
    }
}
