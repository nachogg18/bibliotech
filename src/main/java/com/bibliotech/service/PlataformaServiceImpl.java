package com.bibliotech.service;

import com.bibliotech.entity.Plataforma;
import com.bibliotech.repository.PlataformaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PlataformaServiceImpl implements PlataformaService {

    private final PlataformaRepository plataformaRepository;

    @Override
    public List<Plataforma> findAll() {
        return plataformaRepository.findByFechaBajaNull();
    }

    @Override
    public Optional<Plataforma> findById(Long id) {
        return plataformaRepository.findById(id);
    }

    @Override
    public Plataforma save(Plataforma plataforma) {
        return plataformaRepository.save(plataforma);
    }

    @Override
    public Plataforma edit(Plataforma plataforma, Long id) {
        if (plataformaRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        plataforma.setId(id);
        return plataformaRepository.save(plataforma);
    }

    @Override
    public void delete(Long id) {
        Plataforma plataforma = plataformaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );

        plataforma.setId(id);
        plataforma.setFechaBaja(Instant.now());

        plataformaRepository.save(plataforma);
    }
}
