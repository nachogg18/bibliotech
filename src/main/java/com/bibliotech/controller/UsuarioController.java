package com.bibliotech.controller;

import com.bibliotech.entity.Usuario;
import com.bibliotech.repository.UsuarioRepository;
import com.bibliotech.service.UsuarioServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/usuarios")
@Log4j2
public class UsuarioController extends BaseControllerImpl<Usuario, UsuarioServiceImpl> {
    @Autowired
    public UsuarioRepository usuarioRepository;
}
