package com.bibliotech.service;

import com.bibliotech.entity.Plataforma;
import com.bibliotech.repository.PlataformaRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlataformaServiceImpl implements PlataformaService {

    private final PlataformaRepository plataformaRepository;

    public PlataformaServiceImpl(PlataformaRepository plataformaRepository) {
        this.plataformaRepository = plataformaRepository;
    }

    @Override
    public List<Plataforma> findAll() {
        return plataformaRepository.findByFechaBajaNull();
    }

    @Override
    public Plataforma save(Plataforma plataforma) {
        return plataformaRepository.save(plataforma);
    }

    @Override
    public Plataforma edit(Plataforma plataforma, Long id) {
        if (plataformaRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return plataformaRepository.save(plataforma);
    }

    @Override
    public void delete(Long id) {
        Plataforma plataforma = plataformaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );

        plataforma.setFechaBaja(Instant.now());

        plataformaRepository.save(plataforma);
    }
}
