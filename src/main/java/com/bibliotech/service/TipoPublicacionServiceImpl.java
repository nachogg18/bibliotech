package com.bibliotech.service;

import com.bibliotech.entity.TipoPublicacion;
import com.bibliotech.repository.TipoPublicacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoPublicacionServiceImpl implements TipoPublicacionService {
    private final TipoPublicacionRepository tipoPublicacionRepository;

    public TipoPublicacionServiceImpl(TipoPublicacionRepository tipoPublicacionRepository) {
        this.tipoPublicacionRepository = tipoPublicacionRepository;
    }

    @Override
    public List<TipoPublicacion> findAll() {
        return tipoPublicacionRepository.findAll();
    }
}
