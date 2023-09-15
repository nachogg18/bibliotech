package com.bibliotech.service;

import com.bibliotech.entity.TipoPublicacion;
import com.bibliotech.repository.TipoPublicacionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

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
