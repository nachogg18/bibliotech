package com.bibliotech.service;

import com.bibliotech.entity.Parametro;

import java.util.List;
import java.util.Optional;

public interface ParametroService {

    List<Parametro> obtenerParametros();
    Optional<Parametro> obtenerParametroPorNombre(String nombre);

    Parametro actualizarParametro(String nombre, String valor);

    Parametro guardarParametro(Parametro parametro);
}