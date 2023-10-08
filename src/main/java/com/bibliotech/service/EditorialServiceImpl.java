package com.bibliotech.service;

import com.bibliotech.entity.Editorial;
import com.bibliotech.repository.EditorialRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EditorialServiceImpl implements EditorialService {

    private final EditorialRepository editorialRepository;

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
    public Optional<Editorial> delete(Long id) {
        Optional<Editorial> editorialOptional = editorialRepository.findById(id);
        if (editorialOptional.isPresent()) {
            Editorial editorial = editorialOptional.get();
            if (editorial.getFechaBaja() != null)
                editorialOptional = Optional.empty();
            else {
                editorial.setFechaBaja(Instant.now());
                editorialOptional = Optional.of(editorialRepository.save(editorial));
            }
        }
        return editorialOptional;
    }
}
