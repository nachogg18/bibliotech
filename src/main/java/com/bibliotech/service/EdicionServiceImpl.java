package com.bibliotech.service;

import com.bibliotech.entity.Edicion;
import com.bibliotech.repository.EdicionRepository;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EdicionServiceImpl implements EdicionService {

    private final EdicionRepository edicionRepository;

    public EdicionServiceImpl(EdicionRepository edicionRepository) {
        this.edicionRepository = edicionRepository;
    }

    @Override
    public List<Edicion> findAll() {
        return edicionRepository.findByFechaBajaNull();
    }


    @Override
    public Edicion save(Edicion edicion) {
        return edicionRepository.save(edicion);
    }

    @Override
    public Edicion edit(Edicion edicion, Long id) {
        if (edicionRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return edicionRepository.save(edicion);
    }

    @Override
    public void delete(Long id) {
        Edicion edicion = edicionRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );
        edicion.setFechaBaja(new Date());
    }
}
