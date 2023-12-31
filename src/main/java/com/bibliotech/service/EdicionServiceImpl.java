package com.bibliotech.service;

import com.bibliotech.entity.Edicion;
import com.bibliotech.repository.EdicionRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class EdicionServiceImpl implements EdicionService {

    private final EdicionRepository edicionRepository;

    @Override
    public List<Edicion> findAll() {
        return edicionRepository.findByFechaBajaNull();
    }

    @Override
    public Optional<Edicion> findById(Long id) {
        return edicionRepository.findById(id);
    }


    @Override
    public Edicion save(Edicion edicion) {
        return edicionRepository.save(edicion);
    }

    @Override
    public Edicion edit(Edicion edicion, Long id) {
        if (edicionRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        edicion.setId(id);
        return edicionRepository.save(edicion);
    }

    @Override
    public Optional<Edicion> delete(Long id) {
        Optional<Edicion> edicionOptional = edicionRepository.findById(id);
        if(edicionOptional.isPresent()) {
            Edicion edicion = edicionOptional.get();
            if(edicion.getFechaBaja() != null)
                edicionOptional = Optional.empty();
            else {
                edicion.setFechaBaja(Instant.now());
                edicion.setId(id);
                edicionOptional = Optional.of(edicionRepository.save(edicion));
            }
        }
        return edicionOptional;
    }
}
