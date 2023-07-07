package com.bibliotech.controller;

import com.bibliotech.entity.Publication;
import com.bibliotech.service.PublicationServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/publications")
public class PublicationController extends BaseControllerImpl<Publication, PublicationServiceImpl> {
}

