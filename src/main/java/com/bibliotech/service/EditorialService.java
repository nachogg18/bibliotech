package com.bibliotech.service;

import com.bibliotech.entity.Editorial;

import java.util.List;
import java.util.Optional;

public interface EditorialService {
    List<Editorial> findAll();

    Editorial save(Editorial editorial);

    Editorial edit(Editorial editorial, Long id);

    Optional<Editorial> delete(Long id);
}
