package com.bibliotech.service;

import com.bibliotech.entity.Publication;
import com.bibliotech.repository.PublicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PublicationService {

    private PublicationRepository publicationRepository;

    public ResponseEntity<Publication> getPublication(Long publicationId) {
        Optional<Publication> publication = this.publicationRepository.findById(publicationId);

        return ResponseEntity.of(publication);
    }

    public ResponseEntity<List<Publication>> getPublicationsByName(String publicationName) {
        List<Publication> publications = this.publicationRepository.findPublicationByName(publicationName);

        return ResponseEntity.of(Optional.ofNullable(publications));
    }


    public ResponseEntity<Publication> savePublication(Publication publication) {
        Publication publicationSaved = this.publicationRepository.save(publication);

        return ResponseEntity.of(Optional.of(publicationSaved));
    }

}
