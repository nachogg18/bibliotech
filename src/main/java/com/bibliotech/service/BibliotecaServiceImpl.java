package com.bibliotech.service;

import com.bibliotech.dto.BibliotecaDetalleDTO;
import com.bibliotech.dto.UbicacionResponseDTO;
import com.bibliotech.entity.Biblioteca;
import com.bibliotech.repository.BibliotecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BibliotecaServiceImpl implements BibliotecaService {

    private final BibliotecaRepository bibliotecaRepository;

    @Override
    public Biblioteca findOne(Long id){
        Biblioteca biblioteca;
        try {
            biblioteca = bibliotecaRepository.findOneById(id);
        } catch (Exception e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bilbioteca no entontrada");
        }
        return biblioteca;
    }

    @Override
    public List<Biblioteca> findAll() {
        return bibliotecaRepository.findAll();
    }

    @Override
    public Biblioteca save(Biblioteca biblioteca) {
        return bibliotecaRepository.save(biblioteca);
    }

    @Override
    public BibliotecaDetalleDTO findBibliotecaDetalle(Long id) {
        Biblioteca biblioteca = bibliotecaRepository.findOneById(id);
        return BibliotecaDetalleDTO.builder()
                .nombre(biblioteca.getNombre())
                .contacto(biblioteca.getContacto())
                .ubicacion(biblioteca.getUbicacion())
                .ubicaciones(biblioteca.getUbicacionList().stream().map(
                        ubicacion -> UbicacionResponseDTO.builder()
                                .descripcion(ubicacion.getDescripcion())
                                .fechaBaja(ubicacion.getFechaBaja()==null ? null : ubicacion.getFechaBaja().truncatedTo(ChronoUnit.DAYS))
                                .fechaAlta(ubicacion.getFechaAlta().truncatedTo(ChronoUnit.DAYS))
                                .ocupada(ubicacion.isOcupada())
                                .build()
                ).toList())
                .fechaBaja(biblioteca.getFechaBaja()==null ? null : biblioteca.getFechaBaja().truncatedTo(ChronoUnit.DAYS))
                .fechaAlta(biblioteca.getFechaAlta().truncatedTo(ChronoUnit.DAYS))
                .build();
    }

    @Override
    public List<UbicacionResponseDTO> findBibliotecaUbicaciones(Long id) {
        Biblioteca biblioteca = bibliotecaRepository.findOneById(id);
        return biblioteca.getUbicacionList().stream().map(
                ubicacion -> UbicacionResponseDTO.builder()
                        .descripcion(ubicacion.getDescripcion())
                        .fechaBaja(ubicacion.getFechaBaja()==null ? null : ubicacion.getFechaBaja().truncatedTo(ChronoUnit.DAYS))
                        .fechaAlta(ubicacion.getFechaAlta().truncatedTo(ChronoUnit.DAYS))
                        .ocupada(ubicacion.isOcupada())
                        .build()
        ).toList();
    }

}
