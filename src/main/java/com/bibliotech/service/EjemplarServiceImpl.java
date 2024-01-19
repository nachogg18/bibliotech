package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.repository.EjemplarEstadoRepository;
import com.bibliotech.repository.EjemplarRepository;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EjemplarServiceImpl implements EjemplarService {

    private final EjemplarRepository ejemplarRepository;

    private final PublicacionService publicacionService;

    private final UbicacionService ubicacionService;

    private final EjemplarEstadoRepository ejemplarEstadoRepository;

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

        ubicacionService.findById(ejemplar.getUbicacion().getId()).orElseThrow(() -> new RuntimeException("Error en la ubicación")).setOcupada(false);

        ejemplarRepository.save(ejemplar);

        Ubicacion ubicacion = ejemplar.getUbicacion();
        ubicacion.setOcupada(false);
        ubicacionService.saveChanges(ubicacion);
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
    public List<ComentarioDTO> getAllComentarios(Long id){
        List<Comentario> comentarios = ejemplarRepository.findComentariosByEjemplarId(id).stream().filter(comentario -> !Objects.nonNull(comentario.getFechaBaja())).toList();
        List<ComentarioDTO> comentarioDTOS = comentarios.stream().map( comentario -> {
            ComentarioDTO dto = new ComentarioDTO();
            dto.setId(comentario.getId());
            dto.setComentario(comentario.getComentario());

//            Instant fecha = comentario.getFechaAlta();
//            ZoneId zonaArgentina = ZoneId.of("America/Argentina/Buenos_Aires");
//            // Crear un formateador de fecha y hora
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
//            // Formatear el Instant en la zona horaria de Argentina
//            String formattedDateTime = fecha.atZone(zonaArgentina).format(formatter);
//
//            dto.setFecha(formattedDateTime);
            dto.setFecha(comentario.getFechaAlta());
            dto.setCalificacion(comentario.getCalificacion());
            dto.setAltaUsuario(comentario.getAltaUser().getFirstName() + ' ' + comentario.getAltaUser().getLastName());
            return dto;
        }).toList();

        return comentarioDTOS;
    };


    @Override
    public boolean exists(Long id) {
        return ejemplarRepository.existsById(id);
    }

    @Override
    public EditEjemplarDTO repararEjemplar(Long id){
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe ejemplar con id %s", id)));
        EjemplarEstado ejemplarEstado = ejemplar.getEjemplarEstadoList().stream()
                .filter(estado -> estado.getFechaFin()==null)
                .findFirst()
                .orElse(null);

        if (ejemplarEstado == null) throw new ValidationException("Error con el ejemplar");
        if (ejemplarEstado.getEstadoEjemplar() != EstadoEjemplar.DISPONIBLE) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ejemplar no está en disponible");

        ejemplarEstado.setFechaFin(Instant.now());
        EjemplarEstado nuevoEstado = new EjemplarEstado();
        nuevoEstado.setEstadoEjemplar(EstadoEjemplar.EN_REPARACION);
        ejemplarEstadoRepository.save(nuevoEstado);
        ejemplar.getEjemplarEstadoList().add(nuevoEstado);
        ejemplarRepository.save(ejemplar);

        return EditEjemplarDTO.builder()
                .idUbicacion(ejemplar.getUbicacion().getId())
                .serialNFC(ejemplar.getSerialNFC())
                .estadoEjemplar(nuevoEstado.getEstadoEjemplar().name())
                .build();
    }

    @Override
    public EditEjemplarDTO extraviarEjemplar(Long id){
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe ejemplar con id %s", id)));
        EjemplarEstado ejemplarEstado = ejemplar.getEjemplarEstadoList().stream()
                .filter(estado -> estado.getFechaFin()==null)
                .findFirst()
                .orElse(null);

        if (ejemplarEstado == null) throw new ValidationException("Error con el ejemplar");
        if (ejemplarEstado.getEstadoEjemplar() != EstadoEjemplar.DISPONIBLE) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ejemplar no está en disponible");

        ejemplarEstado.setFechaFin(Instant.now());
        EjemplarEstado nuevoEstado = new EjemplarEstado();
        nuevoEstado.setEstadoEjemplar(EstadoEjemplar.EXTRAVIADO);
        ejemplarEstadoRepository.save(nuevoEstado);
        ejemplar.getEjemplarEstadoList().add(nuevoEstado);
        ejemplarRepository.save(ejemplar);

        return EditEjemplarDTO.builder()
                .idUbicacion(ejemplar.getUbicacion().getId())
                .serialNFC(ejemplar.getSerialNFC())
                .estadoEjemplar(nuevoEstado.getEstadoEjemplar().name())
                .build();
    }

    @Override
    public EditEjemplarDTO habilitarEjemplar(Long id){
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No existe ejemplar con id %s", id)));
        EjemplarEstado ejemplarEstado = ejemplar.getEjemplarEstadoList().stream()
                .filter(estado -> estado.getFechaFin()==null)
                .findFirst()
                .orElse(null);

        if (ejemplarEstado == null) throw new ValidationException("Error con el ejemplar");
        if (ejemplarEstado.getEstadoEjemplar() != EstadoEjemplar.EN_REPARACION && ejemplarEstado.getEstadoEjemplar() != EstadoEjemplar.EXTRAVIADO) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ejemplar no está en disponible");

        ejemplarEstado.setFechaFin(Instant.now());
        EjemplarEstado nuevoEstado = new EjemplarEstado();
        nuevoEstado.setEstadoEjemplar(EstadoEjemplar.DISPONIBLE);
        ejemplarEstadoRepository.save(nuevoEstado);
        ejemplar.getEjemplarEstadoList().add(nuevoEstado);
        ejemplarRepository.save(ejemplar);

        return EditEjemplarDTO.builder()
                .idUbicacion(ejemplar.getUbicacion().getId())
                .serialNFC(ejemplar.getSerialNFC())
                .estadoEjemplar(nuevoEstado.getEstadoEjemplar().name())
                .build();
    }

    @Override
    public EjemplarNFCDTO busquedaEjemplarNFC(String serialNFC){
        try{
            List<Object[]> ejemplaresNFC = ejemplarRepository.obtenerEjemplarNFC(serialNFC);
            List<EjemplarNFCDTO> ejemplares = new ArrayList<EjemplarNFCDTO>();

            ejemplaresNFC.forEach(po -> {
                Long idEjemplar = (Long) po[0]; //ID EJEMPLAR
                float valoracion = ((BigDecimal) po[1]).floatValue(); //VALORACION
                String ubicacion = (String) po[2]; //UBICACION
                String biblioteca = (String) po[3]; //BIBLIOTECA
                Long idPublicacion = (Long) po[4]; //ID PUBLICACION
                String titulo = (String) po[5]; //TITULO
                String sinopsis = (String) po[6]; //SINOPSIS
                Integer anio = (Integer) po[7];//AÑO
                String tipo = (String) po[8];//TIPO PUBLICACION
                String isbn = (String) po[9];//ISBN
                List<String> editoriales = Arrays.stream(((String) po[10]).split(";")).toList();//EDITORIALES
                List<String> autores = Arrays.stream(((String) po[11]).split(";")).toList();//AUTOR
                String edicion = (String) po[12];
                ejemplares.add(EjemplarNFCDTO.builder()
                        .idEjemplar(idEjemplar)
                        .valoracion(valoracion)
                        .ubicacion(ubicacion)
                        .biblioteca(biblioteca)
                        .idPublicacion(idPublicacion)
                        .nombrePublicacion(titulo)
                        .sinopsisPublicacion(sinopsis)
                        .anio(anio)
                        .tipoPublicacion(tipo)
                        .isbn(isbn)
                        .editoriales(editoriales)
                        .autores(autores)
                        .edicion(edicion)
                        .build());
            });

            return ejemplares.get(0);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al buscar ejemplar -> "+e.getMessage());
        }
    }
}
