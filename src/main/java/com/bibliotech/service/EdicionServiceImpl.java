package com.bibliotech.service;

import com.bibliotech.entity.Edicion;
import com.bibliotech.repository.EdicionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EdicionServiceImpl implements EdicionService {

    private final EdicionRepository edicionRepository;

    public EdicionServiceImpl(EdicionRepository edicionRepository) {
        this.edicionRepository = edicionRepository;
    }

    @Override
    public List<Edicion> findAll() {
        return edicionRepository.findAll();
    }
}
