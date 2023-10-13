package com.bibliotech.service;

import com.bibliotech.entity.TipoMulta;
import com.bibliotech.repository.TipoMultaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TipoMultaServiceImpl implements TipoMultaService {

    private final TipoMultaRepository tipoMultaRepository;

    @Override
    public List<TipoMulta> findAll() {
        return tipoMultaRepository.findByFechaBajaNull();
    }


    @Override
    public TipoMulta save(TipoMulta tipoMulta) {
        return tipoMultaRepository.save(tipoMulta);
    }

    @Override
    public TipoMulta edit(TipoMulta tipoMulta, Long id) {
        if (tipoMultaRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        tipoMulta.setId(id);
        return tipoMultaRepository.save(tipoMulta);
    }

    @Override
    public Optional<TipoMulta> delete(Long id) {
        Optional<TipoMulta> tipoMultaOptional = tipoMultaRepository.findById(id);
        if(tipoMultaOptional.isPresent()) {
            TipoMulta tipoMulta = tipoMultaOptional.get();
            if(tipoMulta.getFechaBaja() != null)
                tipoMultaOptional = Optional.empty();
            else {
                tipoMulta.setId(id);
                tipoMulta.setFechaBaja(Instant.now());
                tipoMultaOptional = Optional.of(tipoMultaRepository.save(tipoMulta));
            }
        }
        return tipoMultaOptional;
    }
}
