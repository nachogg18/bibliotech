package com.bibliotech.service;

import com.bibliotech.dto.ParametroDTO;
import com.bibliotech.entity.Parametro;
import java.util.List;

public interface ParametroService {

    List<Parametro> obtenerParametros();
    Parametro obtenerParametroPorNombre(String nombre);

    Parametro actualizarParametro(String nombre, ParametroDTO body);

    Parametro guardarParametro(Parametro parametro);

    Long count();
}