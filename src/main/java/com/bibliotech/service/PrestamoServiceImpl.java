package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.EjemplarEstadoRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.UserService;
import com.bibliotech.utils.RoleUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl extends BaseServiceImpl<Prestamo, Long> implements PrestamoService{
    @Autowired
    private PrestamosRepository prestamosRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EjemplarService ejemplarService;
    @Autowired
    private PrestamoEstadoRepository prestamoEstadoRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private EjemplarEstadoRepository ejemplarEstadoRepository;

    public PrestamoServiceImpl (BaseRepository<Prestamo, Long> baseRepository, UserService userService) {
        super(baseRepository);
    }

    @Override
    public DetallePrestamoDTO getDetallePrestamo (Long id) {
        Prestamo prestamo = prestamosRepository.findById(id).orElseThrow(() -> new ValidationException(String.format("No existe prestamo con el id %s", id)));

        List<RenovacionDTO> renovaciones = new ArrayList<>();

        if (prestamo.getFechasRenovaciones() != null && !prestamo.getFechasRenovaciones().isEmpty()) {
            Instant lastInstant = prestamo.getFechaInicioEstimada();

            for (Instant instant : prestamo.getFechasRenovaciones()) {
                renovaciones.add(RenovacionDTO.builder()
                        .fechaInicioRenovacion(lastInstant)
                        .fechaFinRenovacion(instant)
                        .build());
                lastInstant = instant;
            }
        } else {
            if (prestamo.getFechaFinEstimada() != null) {
                renovaciones.add(RenovacionDTO.builder()
                        .fechaInicioRenovacion(prestamo.getFechaInicioEstimada())
                        .fechaFinRenovacion(prestamo.getFechaFinEstimada())
                        .build());
            }
        }

        return DetallePrestamoDTO.builder()
                .nombreApellidoUsuario(prestamo.getUsuario().getFirstName() + " " + prestamo.getUsuario().getLastName())
                .idUsuario(prestamo.getUsuario().getId())
                .legajoUsuario(prestamo.getUsuario().getLegajo())
                .tituloPublicacion(prestamo.getEjemplar().getPublicacion().getTitulo())
                .idEjemplar(prestamo.getEjemplar().getId())
                .fechaInicioPrestamo(prestamo.getFechaInicioEstimada())
                .fechaFinPrestamo(prestamo.getFechaFinEstimada())
                .fechaDevolucion(prestamo.getFechaBaja())
                .estado(prestamo.getEstado().stream().filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null).getEstado().name())
                .renovaciones(renovaciones)
                .build();
    }

    @Override
    public List<PrestamoItemTablaDTO> getPrestamosListTable() {
        List<Prestamo> prestamos = prestamosRepository.findAll();

        return prestamos.stream().map(
                        prestamo -> {
                            return PrestamoItemTablaDTO.builder()
                                    .id(prestamo.getId())
                                    .tituloPublicacion(prestamo.getEjemplar().getPublicacion().getTitulo())
                                    .fechaDesde(prestamo.getFechaAlta())
                                    .fechaHasta(prestamo.getFechaBaja())
                                    .idEjemplar(prestamo.getEjemplar().getId())
                                    .idUsuario(prestamo.getUsuario().getId())
                                    .estado(prestamo.getEstado().stream().filter(estado -> estado.getFechaFin() == null)
                                            .findFirst()
                                            .orElse(null)
                                            .getEstado().name())
                                    .build();
                        })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrestamoResponse crearPrestamo(PrestamoRequest prestamoRequest) {
        //verificaciones
        User usuarioPrestamo = verifyUsuarioYEjemplar(prestamoRequest);
        Ejemplar ejemplar =  ejemplarService.findById(prestamoRequest.getEjemplarID()).orElseThrow(() -> new ValidationException(String.format("No existe ejemplar con id %s", prestamoRequest.getEjemplarID())));
        verifyFechaPrestamos(prestamoRequest.getFechaInicioEstimada(), prestamoRequest.getFechaFinEstimada(), ejemplar);

        //creacion de prestamo
        Prestamo prestamoNuevo = Prestamo.builder()
                .estado(new ArrayList<>())
                .fechaAlta(Instant.now())
                .fechaInicioEstimada(prestamoRequest.getFechaInicioEstimada())
                .fechaFinEstimada(prestamoRequest.getFechaFinEstimada())
                .usuario(usuarioPrestamo)
                .ejemplar(ejemplarService.findById(prestamoRequest.getEjemplarID()).get())
                .build();

        //prestamo presente o futuro
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.EN_ESPERA);

        prestamoEstadoRepository.save(prestamoEstado);
        prestamoNuevo.getEstado().add(prestamoEstado);
        prestamosRepository.save(prestamoNuevo);

        return PrestamoResponse
                .builder()
                .usuarioID(usuarioPrestamo.getId())
                .ejemplarID(ejemplarService.findById(prestamoRequest.getEjemplarID()).get().getId())
                .PrestamoID(prestamoNuevo.getId())
                .fechaInicioEstimada(prestamoNuevo.getFechaInicioEstimada())
                .fechaFinEstimada(prestamoNuevo.getFechaFinEstimada())
                .fechaAlta(prestamoNuevo.getFechaAlta())
                .estado(prestamoNuevo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null).getEstado().name())
                .build();
    }

    private User verifyUsuarioYEjemplar (PrestamoRequest prestamoRequest){
        //ejemplar existente y disponible
        Ejemplar ejemplar = ejemplarService.findById(prestamoRequest.getEjemplarID()).orElseThrow(() -> new ValidationException(String.format("No existe ejemplar con id %s", prestamoRequest.getEjemplarID())));
        EjemplarEstado ultimoEstado = ejemplar.getEjemplarEstadoList().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (ultimoEstado == null) throw new ValidationException(String.format("Error en el ejemplar con id %s", prestamoRequest.getEjemplarID()));
        if (ultimoEstado.getEstadoEjemplar() == EstadoEjemplar.EN_REPARACION || ultimoEstado.getEstadoEjemplar() == EstadoEjemplar.EXTRAVIADO) throw new ValidationException(String.format("El ejemplar con el id %s no está disponible", prestamoRequest.getEjemplarID()));

        //usuario existente, rol correspondiente y habilitado
        User usuarioAutenticado = authenticationService.getActiveUser().orElseThrow(() -> new ValidationException("no authenticated user"));

        if (!usuarioAutenticado.isEnabled()) throw new ValidationException(String.format("El usuario no está habilitado"));
        Role rolActual = usuarioAutenticado.getRoles().stream()
                .filter(rol -> rol.getEndDate() == null)
                .findFirst()
                .orElse(null);
        if (rolActual.equals(RoleUtils.DEFAULT_ROLE_USER)) {
            return usuarioAutenticado;
        } else {
            return userService.findById(prestamoRequest.getUsuarioID()).orElseThrow(() -> new ValidationException(String.format("No existe usuario con id %s", prestamoRequest.getUsuarioID())));
        }
    }

    private void verifyFechaPrestamos (Instant fechaInicio, Instant fechaFin, Ejemplar ejemplar) {
        //verificar que fecha fin > fecha inicio
        if(fechaInicio.isAfter(fechaFin)) throw new ValidationException("La fecha inicio debe estar antes de la fecha fin");

        //comparar con dias maximos y minimos parametrizados
        // if(fechaInicio.until(fechaFin, ChronoUnit.DAYS) > días max parametrizados) throw new ValidationException(String.format("El periodo de tiempo supera el permitido"))
        // if(fechaInicio.until(fechaFin, ChronoUnit.DAYS) < días min parametrizados) throw new ValidationException(String.format("El periodo de tiempo supera el permitido"))

        //comparar overlap con otras fechas
        if (!ejemplar.getPrestamos().isEmpty()) {
            List<Prestamo> prestamosEnEspera = ejemplar.getPrestamos().stream()
                    .filter(prestamo -> prestamo.getEstado().stream()
                            .anyMatch(estado -> (estado.getEstado() == EstadoPrestamo.EN_ESPERA || estado.getEstado() == EstadoPrestamo.ACTIVO) && estado.getFechaFin() == null))
                    .toList();
            boolean prestamoOverlap = prestamosEnEspera.stream()
                    .anyMatch(subEntity -> subEntity.overlapsWith(fechaInicio, fechaFin));
            if (prestamoOverlap)
                throw new ValidationException(String.format("El periodo de tiempo se superpone con prestamos existentes"));
        } else {
            ejemplar.setPrestamos(new ArrayList<>());
        }
    }

    @Override
    public List<FindPrestamoDTO> getPrestamosByUserId(Long idUsuario) {
        return prestamosRepository.findPrestamoByUsuarioId(idUsuario)
                .stream().map(
                        prestamo -> FindPrestamoDTO
                                .builder()
                                .id(prestamo.getId())
                                .publicacion(prestamo.getEjemplar() == null ? null : prestamo.getEjemplar().getPublicacion().getTitulo())
                                .ejemplar(prestamo.getEjemplar() == null ? null : prestamo.getEjemplar().getId())
                                .estado(prestamo.getEstado().size() == 0 ? null : prestamo.getEstado().stream().filter(pe -> pe.getFechaFin() == null).toList().get(0).getEstado().name())
                                .fechaInicio(prestamo.getFechaInicioEstimada())
                                .build()
                ).toList();
    }


    @Override
    public PrestamoResponse checkOutPrestamo(Long id) {
        Prestamo prestamo = prestamosRepository.findById(id).orElseThrow(() -> new ValidationException(String.format("No existe prestamo con id %s", id)));
        Instant currentTime = Instant.now();

        //verificacion estado prestamo
        PrestamoEstado estadoPrestamo = prestamo.getEstado().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (estadoPrestamo == null) throw new ValidationException("Error con el préstamo");
        if (estadoPrestamo.getEstado() != EstadoPrestamo.EN_ESPERA) throw new ValidationException("El préstamo no está en espera");

        if (currentTime.isBefore(prestamo.getFechaInicioEstimada())) {
            throw new ValidationException("Préstamo aún no comienza");
        }

        //if (!currentTime.isBefore(prestamo.getFechaInicioEstimada()))


        //verificacion estado ejemplar
        EjemplarEstado estadoEjemplar = prestamo.getEjemplar().getEjemplarEstadoList().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (estadoEjemplar == null) throw new ValidationException("Error con el ejemplar");
        if (estadoEjemplar.getEstadoEjemplar() != EstadoEjemplar.DISPONIBLE) throw new ValidationException("El ejemplar no está disponible");

        //cambio de estados
        estadoPrestamo.setFechaFin(Instant.now());
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.ACTIVO);
        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);

        estadoEjemplar.setFechaFin(Instant.now());
        EjemplarEstado ejemplarEstado = new EjemplarEstado();
        ejemplarEstado.setEstadoEjemplar(EstadoEjemplar.PRESTADO);
        ejemplarEstadoRepository.save(ejemplarEstado);
        prestamo.getEjemplar().getEjemplarEstadoList().add(ejemplarEstado);

        //guardar cambios
        prestamoEstadoRepository.save(estadoPrestamo);
        ejemplarEstadoRepository.save(estadoEjemplar);
        prestamosRepository.save(prestamo);

        return PrestamoResponse
                .builder()
                .usuarioID(prestamo.getUsuario().getId())
                .ejemplarID(prestamo.getEjemplar().getId())
                .PrestamoID(prestamo.getId())
                .fechaInicioEstimada(prestamo.getFechaInicioEstimada())
                .fechaFinEstimada(prestamo.getFechaFinEstimada())
                .fechaAlta(prestamo.getFechaAlta())
                .estado(Objects.requireNonNull(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null)).getEstado().name())
                .build();
    }

    @Override
    public PrestamoResponse checkInPrestamo(Long id) {
        Prestamo prestamo = prestamosRepository.findById(id).orElseThrow(() -> new ValidationException(String.format("No existe prestamo con id %s", id)));
        Instant currentTime = Instant.now();

        //verificacion estado prestamo
        PrestamoEstado estadoPrestamo = prestamo.getEstado().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (estadoPrestamo == null) throw new ValidationException("Error con el préstamo");
        if (!(estadoPrestamo.getEstado() == EstadoPrestamo.ACTIVO || estadoPrestamo.getEstado() == EstadoPrestamo.RENOVADO || estadoPrestamo.getEstado() == EstadoPrestamo.VENCIDO)) throw new ValidationException("El préstamo no activo");
        if (currentTime.isBefore(prestamo.getFechaInicioEstimada())) {
            throw new ValidationException("Préstamo aún no comienza");
        }

        //verificacion estado ejemplar
        EjemplarEstado estadoEjemplar = prestamo.getEjemplar().getEjemplarEstadoList().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (estadoEjemplar == null) throw new ValidationException("Error con el ejemplar");
        if (estadoEjemplar.getEstadoEjemplar() != EstadoEjemplar.PRESTADO) throw new ValidationException("El ejemplar no está disponible");

        //cambio de estados
        estadoPrestamo.setFechaFin(Instant.now());
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.DEVUELTO);
        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);

        estadoEjemplar.setFechaFin(Instant.now());
        EjemplarEstado ejemplarEstado = new EjemplarEstado();
        ejemplarEstado.setEstadoEjemplar(EstadoEjemplar.DISPONIBLE);
        ejemplarEstadoRepository.save(ejemplarEstado);
        prestamo.getEjemplar().getEjemplarEstadoList().add(ejemplarEstado);

        prestamo.setFechaBaja(Instant.now());

        //guardar cambios
        prestamoEstadoRepository.save(estadoPrestamo);
        ejemplarEstadoRepository.save(estadoEjemplar);
        prestamosRepository.save(prestamo);

        return PrestamoResponse
                .builder()
                .usuarioID(prestamo.getUsuario().getId())
                .ejemplarID(prestamo.getEjemplar().getId())
                .PrestamoID(prestamo.getId())
                .fechaInicioEstimada(prestamo.getFechaInicioEstimada())
                .fechaFinEstimada(prestamo.getFechaFinEstimada())
                .fechaAlta(prestamo.getFechaAlta())
                .estado(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null).getEstado().name())
                .build();
    }

    @Override
    public PrestamoResponse cancelarPrestamo(Long id) {
        Prestamo prestamo = prestamosRepository.findById(id).orElseThrow(() -> new ValidationException(String.format("No existe prestamo con id %s", id)));
        Instant currentTime = Instant.now();

        //verificacion estado prestamo
        PrestamoEstado estadoPrestamo = prestamo.getEstado().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (estadoPrestamo == null) throw new ValidationException("Error con el préstamo");
        if (estadoPrestamo.getEstado() != EstadoPrestamo.EN_ESPERA) throw new ValidationException("El préstamo no está en espera");

        //cambio de estados
        estadoPrestamo.setFechaFin(Instant.now());
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.CANCELADO);
        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);

        prestamosRepository.save(prestamo);

        return PrestamoResponse
                .builder()
                .usuarioID(prestamo.getUsuario().getId())
                .ejemplarID(prestamo.getEjemplar().getId())
                .PrestamoID(prestamo.getId())
                .fechaInicioEstimada(prestamo.getFechaInicioEstimada())
                .fechaFinEstimada(prestamo.getFechaFinEstimada())
                .fechaAlta(prestamo.getFechaAlta())
                .estado(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null).getEstado().name())
                .build();
    }

    @Override
    public PrestamoResponse renovarPrestamo(Long id, RenovacionDTO req) {
        Prestamo prestamo = prestamosRepository.findById(id).orElseThrow(() -> new ValidationException(String.format("No existe prestamo con id %s", id)));
        Instant currentTime = Instant.now();

        //verificacion estado prestamo
        PrestamoEstado estadoPrestamo = prestamo.getEstado().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (estadoPrestamo == null) throw new ValidationException("Error con el préstamo");
        if (estadoPrestamo.getEstado() != EstadoPrestamo.ACTIVO) throw new ValidationException("El préstamo no está activo");

        verifyFechaPrestamos(req.getFechaInicioRenovacion(), req.getFechaFinRenovacion(), prestamo.getEjemplar());

        if (prestamo.getFechasRenovaciones() != null /*&& prestamo.getFechasRenovaciones().size() < maxRenovaciones*/){
            prestamo.getFechasRenovaciones().add(Instant.now());
        }

        //cambio de estados
        estadoPrestamo.setFechaFin(Instant.now());
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.CANCELADO);
        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);

        prestamosRepository.save(prestamo);

        return PrestamoResponse
                .builder()
                .usuarioID(prestamo.getUsuario().getId())
                .ejemplarID(prestamo.getEjemplar().getId())
                .PrestamoID(prestamo.getId())
                .fechaInicioEstimada(prestamo.getFechaInicioEstimada())
                .fechaFinEstimada(prestamo.getFechaFinEstimada())
                .fechaAlta(prestamo.getFechaAlta())
                .estado(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null).getEstado().name())
                .build();
    }

    @Override
    public PrestamoResponse extravioPrestamo(Long id) {
        Prestamo prestamo = prestamosRepository.findById(id).orElseThrow(() -> new ValidationException(String.format("No existe prestamo con id %s", id)));
        Instant currentTime = Instant.now();

        //verificacion estado prestamo
        PrestamoEstado estadoPrestamo = prestamo.getEstado().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (estadoPrestamo == null) throw new ValidationException("Error con el préstamo");
        if (!(estadoPrestamo.getEstado() == EstadoPrestamo.ACTIVO || estadoPrestamo.getEstado() == EstadoPrestamo.RENOVADO || estadoPrestamo.getEstado() == EstadoPrestamo.VENCIDO)) throw new ValidationException("El préstamo no está activo");

        //verificacion estado ejemplar
        EjemplarEstado estadoEjemplar = prestamo.getEjemplar().getEjemplarEstadoList().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (estadoEjemplar == null) throw new ValidationException("Error con el ejemplar");
        if (estadoEjemplar.getEstadoEjemplar() != EstadoEjemplar.PRESTADO) throw new ValidationException("El ejemplar no está prestado");

        //cambio de estados
        estadoPrestamo.setFechaFin(Instant.now());
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.EXTRAVIADO);
        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);

        estadoEjemplar.setFechaFin(Instant.now());
        EjemplarEstado ejemplarEstado = new EjemplarEstado();
        ejemplarEstado.setEstadoEjemplar(EstadoEjemplar.EXTRAVIADO);
        ejemplarEstadoRepository.save(ejemplarEstado);
        prestamo.getEjemplar().getEjemplarEstadoList().add(ejemplarEstado);

        prestamosRepository.save(prestamo);

        return PrestamoResponse
                .builder()
                .usuarioID(prestamo.getUsuario().getId())
                .ejemplarID(prestamo.getEjemplar().getId())
                .PrestamoID(prestamo.getId())
                .fechaInicioEstimada(prestamo.getFechaInicioEstimada())
                .fechaFinEstimada(prestamo.getFechaFinEstimada())
                .fechaAlta(prestamo.getFechaAlta())
                .estado(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null).getEstado().name())
                .build();
    }
}
