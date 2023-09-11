package com.bibliotech.service;

import com.bibliotech.entity.Editorial;
import com.bibliotech.repository.EditorialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditorialServiceImpl implements EditorialService {
    private final EditorialRepository editorialRepository;

    public EditorialServiceImpl(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }

    @Override
    public List<Editorial> findAll() {
        return editorialRepository.findAll();
    }
}
