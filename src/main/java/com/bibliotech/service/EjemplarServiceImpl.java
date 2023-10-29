package com.bibliotech.service;

import com.bibliotech.dto.CrearEjemplarDTO;
import com.bibliotech.dto.EditEjemplarDTO;
import com.bibliotech.dto.EjemplarDetailDTO;
import com.bibliotech.dto.EjemplarResponseDTO;
import com.bibliotech.entity.*;
import com.bibliotech.repository.EjemplarRepository;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                .stream().map(this::mapEjemplarToEjemplarResponseDTO).toList();
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
    public List<EjemplarResponseDTO> findEjemplaresByPublicacionId(Long publicacionId) {
        return ejemplarRepository.findByPublicacionIdAndFechaBajaIsNull(publicacionId)
                .stream().map(this::mapEjemplarToEjemplarResponseDTO).toList();
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

        ubicacion.setOcupada(true);

        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public Ejemplar save(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    @Override
    @Transactional
    public Ejemplar edit(EditEjemplarDTO request, Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        if (request.getSerialNFC() != null && request.getSerialNFC() != ejemplar.getSerialNFC())
            ejemplar.setSerialNFC(request.getSerialNFC());
        if(request.getIdUbicacion() != null && request.getIdUbicacion() != ejemplar.getPublicacion().getId()) {
            Ubicacion ubicacion = ubicacionService.findById(request.getIdUbicacion())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
            ubicacionService.changeOcupada(ejemplar.getUbicacion().getId(), false);
            ubicacion.setOcupada(true);
            ejemplar.setUbicacion(ubicacion);
        }

        EjemplarEstado estadoActual = ejemplar.getEjemplarEstadoList().stream().filter(estado -> Objects.nonNull(estado.getFechaInicio())).findFirst().get();
        if(request.getEstadoEjemplar() != null && estadoActual.getEstadoEjemplar() != EstadoEjemplar.valueOf(request.getEstadoEjemplar())) {
            EjemplarEstado estadoEjemplar = new EjemplarEstado();
            estadoEjemplar.setEstadoEjemplar(EstadoEjemplar.valueOf(request.getEstadoEjemplar().toUpperCase()));
            estadoEjemplar.setFechaInicio(Instant.now());
            // TODO: setear la fechaBaja en estados anteriores
            // creo que esta resuelto abajo con lo que hice (Igna)
            estadoActual.setFechaFin(Instant.now());
            ejemplar.getEjemplarEstadoList().add(estadoEjemplar);
        }

        if(id != ejemplar.getId()) ejemplar.setId(id);

        return ejemplarRepository.save(ejemplar);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );
        EjemplarEstado estadoEjemplar = ejemplar.getEjemplarEstadoList().stream().filter(estado -> !Objects.nonNull(estado.getFechaFin())).findFirst().get();
        estadoEjemplar.setFechaFin(Instant.now());
        if (estadoEjemplar.getEstadoEjemplar() == EstadoEjemplar.DISPONIBLE || estadoEjemplar.getEstadoEjemplar() == EstadoEjemplar.EN_REPARACION){
            EjemplarEstado nuevoEstado = new EjemplarEstado();
            nuevoEstado.setFechaInicio(Instant.now());
            nuevoEstado.setFechaFin(Instant.now());
            ejemplar.getEjemplarEstadoList().add(nuevoEstado);
        } else if (estadoEjemplar.getEstadoEjemplar() != EstadoEjemplar.EXTRAVIADO){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para eliminar el ejemplar, debe estar en DISPONIBLE, EN_REPARACION o EXTRAVIADO");
        }
        ejemplar.setId(id);
        ejemplar.setFechaBaja(Instant.now());
        ejemplarRepository.save(ejemplar);
    }

    @Override
    public Optional<Ejemplar> findById(Long id) {
        return ejemplarRepository.findById(id);
    }


    private EjemplarResponseDTO mapEjemplarToEjemplarResponseDTO(Ejemplar ejemplar) {

        EjemplarResponseDTO response = new EjemplarResponseDTO();

        response.setId(ejemplar.getId());
        response.setTieneComentarios(!ejemplar.getComentarios().isEmpty());
        response.setSerialNFC(ejemplar.getSerialNFC());

        EjemplarEstado estadoFinal = ejemplar.getEjemplarEstadoList().stream().filter(estado -> !Objects.nonNull(estado.getFechaFin())).findFirst().get();

        response.setEstado(estadoFinal.getEstadoEjemplar().toString());

        //String ubicacion = ejemplar.getUbicacion().getDescripcion(); //+ " - " + ejemplar.getUbicacion().getBiblioteca().getNombre();
        response.setUbicacion(ejemplar.getUbicacion());

        float valoracion = ejemplar.getComentarios().size() == 0
                ? 0.00f
                : (float) ejemplar.getComentarios()
                    .stream().mapToInt(Comentario::getCalificacion).sum() / ejemplar.getComentarios().size();
//        DecimalFormat df = new DecimalFormat("#.00");
//        valoracion = Float.parseFloat(df.format(valoracion));
        response.setValoracion(valoracion);

        return response;
    }

    @Override
    public boolean exists(Long id) {
        return ejemplarRepository.existsById(id);
    }
}
