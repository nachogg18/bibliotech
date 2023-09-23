package com.bibliotech.service;

import com.bibliotech.entity.TipoPublicacion;
import com.bibliotech.repository.TipoPublicacionRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TipoPublicacionServiceImpl implements TipoPublicacionService {

    private final TipoPublicacionRepository tipoPublicacionRepository;

    public TipoPublicacionServiceImpl(TipoPublicacionRepository tipoPublicacionRepository) {
        this.tipoPublicacionRepository = tipoPublicacionRepository;
    }

    @Override
    public List<TipoPublicacion> findAll() {
        return tipoPublicacionRepository.findByFechaBajaNull();
    }


    @Override
    public TipoPublicacion save(TipoPublicacion tipoPublicacion) {
        return tipoPublicacionRepository.save(tipoPublicacion);
    }

    @Override
    public TipoPublicacion edit(TipoPublicacion tipoPublicacion, Long id) {
        if (tipoPublicacionRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return tipoPublicacionRepository.save(tipoPublicacion);
    }

    @Override
    public Optional<TipoPublicacion> delete(Long id) {
        Optional<TipoPublicacion> tipoPublicacionOptional = tipoPublicacionRepository.findById(id);
        if(tipoPublicacionOptional.isPresent()) {
            TipoPublicacion tipoPublicacion = tipoPublicacionOptional.get();
            if(tipoPublicacion.getFechaBaja() != null)
                tipoPublicacionOptional = Optional.empty();
            else {
                tipoPublicacion.setFechaBaja(Instant.now());
                tipoPublicacionOptional = Optional.of(tipoPublicacionRepository.save(tipoPublicacion));
            }
        }
        return tipoPublicacionOptional;
    }
}
