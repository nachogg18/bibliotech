package com.bibliotech.repository;

import com.bibliotech.entity.Publication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PublicationRepository extends CrudRepository<Publication, Long> {
    Publication findById(long id);

    List<Publication> findPublicationByName(String name);

    Publication save(Publication publication);

}
