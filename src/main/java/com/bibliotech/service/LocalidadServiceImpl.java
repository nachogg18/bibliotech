package com.bibliotech.service;

import com.bibliotech.dto.CrearLocalidadDTO;
import com.bibliotech.dto.LocalidadDTO;
import com.bibliotech.entity.Localidad;
import com.bibliotech.entity.Provincia;
import com.bibliotech.repository.LocalidadRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class LocalidadServiceImpl implements LocalidadService {

    private final LocalidadRepository localidadRepository;
    private final ProvinciaService provinciaService;

    @Override
    public Optional<Localidad> findByIdAndFechaBajaNull(Long id) {
        return localidadRepository.findByIdAndAndFechaBajaNull(id);
    }

    @Override
    public List<Localidad> findByFechaBajaNull() {
        return localidadRepository.findByFechaBajaNull();
    }

    @Override
    public Localidad crearLocalidad(CrearLocalidadDTO localidadDTO) {
        Provincia provincia = provinciaService.findByIdAndFechaBajaNull(localidadDTO.getProvincia_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Provincia con id %s no existe", localidadDTO.getProvincia_id())));

        return localidadRepository.save(Localidad.builder()
                        .fechaAlta(Instant.now())
                        .nombre(localidadDTO.getNombre())
                        .provincia(provincia)
                        .codigoPostal(localidadDTO.getCodigo_postal())
                        .build()
        );
    }

    @Override
    public List<LocalidadDTO> findByProvinciaId(Long id) {
        return localidadRepository.findByProvinciaId(id)
                .stream().map(
                        LocalidadDTO::toDto
                ).toList();
    }


    @Override
    public Localidad save(Localidad localidad) {
        return localidadRepository.save(localidad);
    }

    @Override
    public Long count() {
        return localidadRepository.count();
    }
}
