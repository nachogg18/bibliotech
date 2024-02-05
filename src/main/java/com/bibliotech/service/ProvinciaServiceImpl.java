package com.bibliotech.service;

import com.bibliotech.dto.CrearProvinciaDTO;
import com.bibliotech.entity.Pais;
import com.bibliotech.entity.Provincia;
import com.bibliotech.repository.ProvinciaRepository;
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
public class ProvinciaServiceImpl implements ProvinciaService {

    private final ProvinciaRepository provinciaRepository;
    private final PaisService paisService;

    @Override
    public Optional<Provincia> findByIdAndFechaBajaNull(Long id) {
        return provinciaRepository.findByIdAndAndFechaBajaNull(id);
    }

    @Override
    public List<Provincia> findByFechaBajaNull() {
        return provinciaRepository.findByFechaBajaNull();
    }

    @Override
    public Provincia crearProvincia(CrearProvinciaDTO provinciaDTO) {
        Pais pais = paisService.findByIdAndFechaBajaNull(provinciaDTO.getPais_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Pais con id %s no existe", provinciaDTO.getPais_id())));

        return provinciaRepository.save(Provincia.builder()
                        .fechaAlta(Instant.now())
                        .nombre(provinciaDTO.getNombre())
                        .pais(pais)
                        .build()
                );
    }

    @Override
    public Provincia save(Provincia provincia) {
        return provinciaRepository.save(provincia);
    }

    @Override
    public Long count() {
        return provinciaRepository.count();
    }
}
