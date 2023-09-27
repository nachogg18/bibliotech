package com.bibliotech.service;

import com.bibliotech.entity.Link;
import com.bibliotech.entity.Plataforma;
import com.bibliotech.repository.LinkRepository;
import com.bibliotech.repository.PlataformaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    @Override
    public List<Link> findAll() {
        return linkRepository.findByFechaBajaNull();
    }

    @Override
    public Link save(Link link) {
        return linkRepository.save(link);
    }

    @Override
    public Link edit(Link link, Long id) {
        if (linkRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return linkRepository.save(link);
    }

    @Override
    public void delete(Long id) {
        Link link = linkRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );

        link.setFechaBaja(Instant.now());

        linkRepository.save(link);
    }
}
