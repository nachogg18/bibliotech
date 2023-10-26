package com.bibliotech.service;

import com.bibliotech.entity.Parametro;
import com.bibliotech.repository.ParametroRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParametroServiceImpl implements ParametroService {
    private final ParametroRepository parametroRepository;

    @Override
    public List<Parametro> obtenerParametros() {
        return parametroRepository.findAll();
    }

    public Optional<Parametro> obtenerParametroPorNombre(String nombre) {
        return parametroRepository.findByNombre(nombre);
    }

    public Parametro  actualizarParametro(String nombre, String valor) {
        Parametro parametro = parametroRepository.findByNombre(nombre).orElseThrow(() -> new ValidationException("parametro no encontrado"));

        parametro.setValor(valor);
        return parametroRepository.save(parametro);

    }

    public Parametro guardarParametro(Parametro parametro) {
        return parametroRepository.save(parametro);
    }


}
