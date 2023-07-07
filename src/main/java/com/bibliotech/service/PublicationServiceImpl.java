package com.bibliotech.service;

import com.bibliotech.entity.Publication;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicationServiceImpl extends BaseServiceImpl<Publication, Long> implements PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;


    public PublicationServiceImpl(BaseRepository<Publication, Long> baseRepository) {
        super(baseRepository);
    }
}
