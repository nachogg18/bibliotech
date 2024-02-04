package com.bibliotech.service;

import com.bibliotech.entity.Link;
import com.bibliotech.repository.LinkRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    @Override
    public List<Link> findAll() {
        return linkRepository.findByFechaBajaNull();
    }

    @Override
    public Optional<Link> findByIdAndFechaBajaNull(Long id) {
        return linkRepository.findByIdAndAndFechaBajaNull(id);
    }

    @Override
    public Link save(Link link) {
        return linkRepository.save(link);
    }

    @Override
    public Link edit(Link link, Long id) {
        if (linkRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        link.setId(id);
        return linkRepository.save(link);
    }

    @Override
    public void delete(Long id) {
        Link link = linkRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );

        link.setId(id);
        link.setFechaBaja(Instant.now());

        linkRepository.save(link);
    }

    @Override
    public Long count() {
    return linkRepository.count();
    }
}
