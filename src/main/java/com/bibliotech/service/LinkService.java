package com.bibliotech.service;

import com.bibliotech.entity.Link;
import java.util.List;
import java.util.Optional;

public interface LinkService {
    List<Link> findAll();

    Optional<Link> findByIdAndFechaBajaNull(Long id);

    Link save(Link link);

    Link edit(Link link, Long id);

    void delete(Long id);
}
