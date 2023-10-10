package com.bibliotech.service;

import com.bibliotech.entity.Ubicacion;
import com.bibliotech.repository.UbicacionRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    @Override
    public List<Ubicacion> findAll() {
        return ubicacionRepository.findByFechaBajaNull();
    }

    @Override
    public Optional<Ubicacion> findById(Long id) {
        return ubicacionRepository.findById(id);
    }


    @Override
    public Ubicacion save(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public Ubicacion edit(Ubicacion ubicacion, Long id) {
        if (ubicacionRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        ubicacion.setId(id);
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> delete(Long id) {
        Optional<Ubicacion> ubicacionOptional = ubicacionRepository.findById(id);
        if (ubicacionOptional.isPresent()) {
            Ubicacion ubicacion = ubicacionOptional.get();
            if (ubicacion.getFechaBaja() != null)
                ubicacionOptional = Optional.empty();
            else {
                ubicacion.setId(id);
                ubicacion.setFechaBaja(Instant.now());
                ubicacionOptional = Optional.of(ubicacionRepository.save(ubicacion));
            }
        }
        return ubicacionOptional;
    }
}
