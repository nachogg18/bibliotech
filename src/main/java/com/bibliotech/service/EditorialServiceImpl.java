package com.bibliotech.service;

import com.bibliotech.entity.Editorial;
import com.bibliotech.repository.EditorialRepository;
import java.util.Date;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EditorialServiceImpl implements EditorialService {
    private final EditorialRepository editorialRepository;

    public EditorialServiceImpl(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }

    @Override
    public List<Editorial> findAll() {
        return editorialRepository.findByFechaBajaNull();
    }

    @Override
    public Editorial save(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    @Override
    public Editorial edit(Editorial editorial, Long id) {
        if (editorialRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return editorialRepository.save(editorial);
    }

    @Override
    public void delete(Long id) {
        Editorial editorial = editorialRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );
        editorial.setFechaBaja(new Date());
    }
}
