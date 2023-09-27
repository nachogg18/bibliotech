package com.bibliotech.service;

import com.bibliotech.entity.Link;
import com.bibliotech.entity.Plataforma;

import java.util.List;

public interface LinkService {
    List<Link> findAll();

    Link save(Link link);

    Link edit(Link link, Long id);

    void delete(Long id);
}
