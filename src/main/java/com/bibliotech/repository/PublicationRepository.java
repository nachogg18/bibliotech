package com.bibliotech.repository;

import com.bibliotech.entity.Publication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends BaseRepository<Publication, Long> {
}
