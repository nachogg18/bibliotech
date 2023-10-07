package com.bibliotech.service;

import com.bibliotech.dto.CrearEjemplarDTO;
import com.bibliotech.entity.Ejemplar;
import com.bibliotech.entity.EjemplarEstado;
import com.bibliotech.entity.EstadoEjemplar;
import com.bibliotech.entity.Publicacion;
import com.bibliotech.repository.EjemplarEstadoRepository;
import com.bibliotech.repository.EjemplarRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EjemplarServiceImpl implements EjemplarService {
    private final EjemplarRepository ejemplarRepository;
    
    private final PublicacionService publicacionService;
    private final EjemplarEstadoRepository ejemplarEstadoRepository;
    

    @Override
    public List<Ejemplar> findAll() {
        return ejemplarRepository.findByFechaBajaNull();
    }

    @Override
    public Ejemplar createEjemplar(CrearEjemplarDTO request) throws Exception {
        Optional<Publicacion> publicacion = publicacionService.findById(request.getIdPublicacion());

        if (publicacion.isEmpty()) {
            throw new RuntimeException(String.format("No existe publicacion con id %s", request.getIdPublicacion()));
        }

        EjemplarEstado estado = EjemplarEstado.builder()
                .estadoEjemplar(EstadoEjemplar.DISPONIBLE)
                .fechaInicio(Instant.now())
                .build();
        List<EjemplarEstado> estadoList = new ArrayList<>();
        estadoList.add(estado);
        ejemplarEstadoRepository.save(estado);

        Ejemplar ejemplar = Ejemplar.builder()
                .ejemplarEstadoList(estadoList)
                .fechaAlta(Instant.now())
                .publicacion(publicacion.get())
                .build();

        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public Ejemplar save(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public Ejemplar edit(Ejemplar ejemplar, Long id) {
        if (ejemplarRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public void delete(Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );
        ejemplar.setFechaBaja(Instant.now());
        ejemplarRepository.save(ejemplar);
    }

    @Override
    public Optional<Ejemplar> findById(Long id) {
        return ejemplarRepository.findById(id);
    }

    @Override
    public boolean exists(Long id) {
        return ejemplarRepository.existsById(id);
    }
}
