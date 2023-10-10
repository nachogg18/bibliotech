package com.bibliotech.service;

import com.bibliotech.dto.CrearEjemplarDTO;

import com.bibliotech.dto.EjemplarDetailDTO;
import com.bibliotech.dto.EjemplarResponseDTO;
import com.bibliotech.entity.*;
import com.bibliotech.repository.EjemplarRepository;

import java.time.Instant;
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

    private final UbicacionService ubicacionService;

    @Override
    public List<EjemplarResponseDTO> findAll() {
        return ejemplarRepository.findByFechaBajaNull()
                .stream().map(
                        e -> {
                            EjemplarResponseDTO response = new EjemplarResponseDTO();
                            response.setId(e.getId());
                            double valoracion = e.getComentarios().size() == 0
                                    ? 0
                                    : e.getComentarios().stream().mapToDouble(Comentario::getCalificacion).sum() / e.getComentarios().size();
                            response.setValoracion(valoracion);
                            return response;
                        }

                ).toList();
    }

    @Override
    public EjemplarDetailDTO findOne(Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("No existe ejemplar con id %s", id)));
        EjemplarDetailDTO dto = new EjemplarDetailDTO();
        dto.setId(ejemplar.getId());
        dto.setNombrePublicacion(ejemplar.getPublicacion().getTitulo());
        dto.setNombreUbicacion(ejemplar.getUbicacion().getBiblioteca().getNombre());
        dto.setSerialNFC(ejemplar.getSerialNFC());

        return dto;
    }

    @Override
    public Ejemplar createEjemplar(CrearEjemplarDTO request) throws Exception {
        Optional<Publicacion> publicacion = publicacionService.findById(request.getIdPublicacion());

        if (publicacion.isEmpty()) {
            throw new RuntimeException(String.format("No existe publicacion con id %s", request.getIdPublicacion()));
        }

        Ubicacion ubicacion = ubicacionService.findById(request.getIdUbicacion())
                .orElseThrow(() -> new RuntimeException(String.format("No existe ubicacion con id %s", request.getIdPublicacion())));

        EjemplarEstado ejemplarEstado = new EjemplarEstado();
        ejemplarEstado.setEstadoEjemplar(EstadoEjemplar.DISPONIBLE);

        Ejemplar ejemplar = Ejemplar.builder()
                .fechaAlta(Instant.now())
                .publicacion(publicacion.get())
                .ubicacion(ubicacion)
                .ejemplarEstadoList(List.of(ejemplarEstado))
                .serialNFC(request.getSerialNFC())
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
}
