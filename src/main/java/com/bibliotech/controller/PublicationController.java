package com.bibliotech.controller;

import com.bibliotech.entity.Publication;
import com.bibliotech.repository.PublicationRepository;
import com.bibliotech.service.PublicationService;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/publication")
public class PublicationController {

    private static final Logger logger = LoggerFactory.getLogger(PublicationController.class);

    private PublicationService publicationService;
    @GetMapping("")
        public ResponseEntity<List<Publication>> getPublications() {
        logger.info("get publication");
        publicationService.savePublication(new Publication(1L, "hola"));
        ResponseEntity<List<Publication>> publications = publicationService.getPublicationsByName("hola");
        return publications;
    };


}

