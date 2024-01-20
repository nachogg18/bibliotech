package com.bibliotech.service;

import com.bibliotech.dto.BibliotecaCreateDTO;
import com.bibliotech.dto.BibliotecaDetalleDTO;
import com.bibliotech.dto.UbicacionResponseDTO;
import com.bibliotech.entity.Biblioteca;
import com.bibliotech.repository.BibliotecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public Biblioteca delete(Long id){
        Biblioteca biblioteca = this.findOne(id);
        biblioteca.setFechaBaja(Instant.now());
        biblioteca.getUbicacionList().forEach(ubicacion -> {
            if (Objects.isNull(ubicacion.getFechaBaja())){
                ubicacion.setFechaBaja(Instant.now());
            }
        });
        bibliotecaRepository.save(biblioteca);
        return biblioteca;
    }

    @Override
    public List<Biblioteca> findAll() {
        return bibliotecaRepository.findAll();
    }

    @Override
    public Biblioteca save(BIbliotecaCreateUpdateDTO biblioteca) {
        Biblioteca biblioteca1 = new Biblioteca();
        biblioteca1.setNombre(biblioteca.getNombre());
        biblioteca1.setUbicacion(biblioteca.getUbicacion());
        biblioteca1.setContacto(biblioteca.getContacto());
        biblioteca1.setUbicacionList(new ArrayList<>());
        biblioteca1.setFechaAlta(Instant.now());
        return bibliotecaRepository.save(biblioteca1);
    }

    @Override
    public Biblioteca saveBiblioteca(Biblioteca biblioteca) {
        return bibliotecaRepository.save(biblioteca);
    }

    @Override
    public BibliotecaDetalleDTO crearBiblioteca(BibliotecaCreateDTO req) {
        Biblioteca bibliotecaNueva = bibliotecaRepository.save(Biblioteca.builder()
                .ubicacionList(new ArrayList<>())
                .nombre(req.getNombre())
                .contacto(req.getContacto())
                .ubicacion(req.getUbicacion())
                .fechaAlta(Instant.now())
                .build()
        );
        return BibliotecaDetalleDTO.builder()
                .nombre(bibliotecaNueva.getNombre())
                .ubicacion(bibliotecaNueva.getUbicacion())
                .contacto(bibliotecaNueva.getContacto())
                .fechaBaja(null)
                .fechaAlta(bibliotecaNueva.getFechaAlta())
                .ubicaciones(null)
                .build();
    }

    @Override
    public BibliotecaDetalleDTO edit(Long id, BibliotecaCreateDTO req) {
        Biblioteca bibliotecaNueva = bibliotecaRepository.getReferenceById(id);
        bibliotecaNueva.setContacto(req.getContacto());
        bibliotecaNueva.setNombre(req.getNombre());
        bibliotecaNueva.setUbicacion(req.getUbicacion());
        bibliotecaRepository.save(bibliotecaNueva);
        return BibliotecaDetalleDTO.builder()
                .nombre(bibliotecaNueva.getNombre())
                .ubicacion(bibliotecaNueva.getUbicacion())
                .contacto(bibliotecaNueva.getContacto())
                .fechaBaja(null)
                .fechaAlta(bibliotecaNueva.getFechaAlta())
                .ubicaciones(null)
                .build();
    }

    @Override
    public BibliotecaDetalleDTO delete(Long id) {
        if (bibliotecaRepository.findAll().size()==1) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe haber como mínimo una biblioteca existente.");
        Biblioteca bibliotecaBorrada = bibliotecaRepository.getReferenceById(id);
        bibliotecaBorrada.setFechaBaja(Instant.now());
        return BibliotecaDetalleDTO.builder()
                .nombre(bibliotecaBorrada.getNombre())
                .ubicacion(bibliotecaBorrada.getUbicacion())
                .contacto(bibliotecaBorrada.getContacto())
                .fechaBaja(null)
                .fechaAlta(bibliotecaBorrada.getFechaAlta())
                .ubicaciones(null)
                .build();
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
                        .id(ubicacion.getId())
                        .descripcion(ubicacion.getDescripcion())
                        .fechaBaja(ubicacion.getFechaBaja()==null ? null : ubicacion.getFechaBaja().truncatedTo(ChronoUnit.DAYS))
                        .fechaAlta(ubicacion.getFechaAlta().truncatedTo(ChronoUnit.DAYS))
                        .ocupada(ubicacion.isOcupada())
                        .build()
        ).toList();
    }

}
