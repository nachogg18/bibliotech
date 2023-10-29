package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.repository.EjemplarRepository;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public Ejemplar save(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public Ejemplar edit(EditEjemplarDTO request, Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));

        if (request.getSerialNFC() != null)
            ejemplar.setSerialNFC(request.getSerialNFC());
        if(request.getIdUbicacion() != null) {
            Ubicacion ubicacion = ubicacionService.findById(request.getIdUbicacion())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found"));
            ejemplar.setUbicacion(ubicacion);
        }
        if(request.getEstadoEjemplar() != null) {
            EjemplarEstado estadoEjemplar = new EjemplarEstado();
            estadoEjemplar.setEstadoEjemplar(EstadoEjemplar.valueOf(request.getEstadoEjemplar().toUpperCase()));
            estadoEjemplar.setFechaInicio(Instant.now());
            // TODO: setear la fechaBaja en estados anteriores
            ejemplar.getEjemplarEstadoList().add(estadoEjemplar);
        }
        ejemplar.setId(id);
        return ejemplarRepository.save(ejemplar);
    }

    @Override
    public void delete(Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        );
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
        float valoracion = ejemplar.getComentarios().size() == 0
                ? 0
                : (float) ejemplar.getComentarios()
                    .stream().mapToInt(Comentario::getCalificacion).sum() / ejemplar.getComentarios().size();
        DecimalFormat df = new DecimalFormat("#.00");
        valoracion = Float.parseFloat(df.format(valoracion));
        response.setValoracion(valoracion);
        return response;
    }

    @Override
    public List<ComentarioDTO> getAllComentarios(Long id){
        List<Comentario> comentarios = ejemplarRepository.findComentariosByEjemplarId(id);
        List<ComentarioDTO> comentarioDTOS = comentarios.stream().map( comentario -> {
            ComentarioDTO dto = new ComentarioDTO();
            dto.setId(comentario.getId());
            dto.setComentario(comentario.getComentario());

            Instant fecha = comentario.getFechaAlta();
            ZoneId zonaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
            // Crear un formateador de fecha y hora
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            // Formatear el Instant en la zona horaria de Argentina
            String formattedDateTime = fecha.atZone(zonaArgentina).format(formatter);

            dto.setFecha(formattedDateTime);
            dto.setCalificacion(comentario.getCalificacion());
            dto.setAltaUsuario(comentario.getAltaUsuario().getFirstName() + ' ' + comentario.getAltaUsuario().getLastName());
            return dto;
        }).toList();

        return comentarioDTOS;
    };


    @Override
    public boolean exists(Long id) {
        return ejemplarRepository.existsById(id);
    }

}
