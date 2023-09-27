package com.bibliotech.service;

import com.bibliotech.entity.Autor;
import com.bibliotech.repository.AutorRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository autorRepository;

    public AutorServiceImpl(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public List<Autor> findAll() {
        return autorRepository.findByFechaBajaNull();
    }


    @Override
    public Autor save(Autor autor) {
        return autorRepository.save(autor);
    }

    @Override
    public Autor edit(Autor autor, Long id) {
        if (autorRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return autorRepository.save(autor);
    }

    @Override
    public Optional<Autor> delete(Long id) {
        Optional<Autor> autorOptional = autorRepository.findById(id);
        if(autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            if(autor.getFechaBaja() != null)
                autorOptional = Optional.empty();
            else {
                autor.setFechaBaja(Instant.now());
                autorOptional = Optional.of(autorRepository.save(autor));
            }
        }
        return autorOptional;
    }
}
