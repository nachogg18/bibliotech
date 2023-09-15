package com.bibliotech.service;

import com.bibliotech.entity.Editorial;
import java.util.List;

public interface EditorialService {
    List<Editorial> findAll();

    Editorial save(Editorial editorial);

    Editorial edit(Editorial editorial, Long id);

    void delete(Long id);
}
