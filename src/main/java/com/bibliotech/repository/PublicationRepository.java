package com.bibliotech.repository;

import com.bibliotech.entity.Publication;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationRepository extends BaseRepository<Publication, Long>, JpaSpecificationExecutor<Publication> {


}
