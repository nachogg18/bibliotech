package com.bibliotech.controller;

import com.bibliotech.entity.Ejemplar;
import com.bibliotech.repository.EjemplarRepository;
import com.bibliotech.service.EjemplarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/ejemplares")
public class EjemplarController extends BaseControllerImpl<Ejemplar, EjemplarServiceImpl>{
    @Autowired
    public EjemplarRepository ejemplarRepository;
}
