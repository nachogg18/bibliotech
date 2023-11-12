package com.bibliotech.service;

import com.bibliotech.entity.Parametro;
import com.bibliotech.repository.ParametroRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

    public Parametro obtenerParametroPorNombre(String nombre) {
        Optional<Parametro> optionalParametro = parametroRepository.findByNombre(nombre);
        if (optionalParametro.isPresent()) return optionalParametro.get();
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Parámetro no encontrado");
    }

    public Parametro  actualizarParametro(String nombre, String valor) {
        Parametro parametro = this.obtenerParametroPorNombre(nombre);
        parametro.setValor(valor);
        return parametroRepository.save(parametro);
    }

    public Parametro guardarParametro(Parametro parametro) {
        return parametroRepository.save(parametro);
    }


}
