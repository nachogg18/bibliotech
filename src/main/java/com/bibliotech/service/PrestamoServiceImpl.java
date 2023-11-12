package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.EjemplarEstadoRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.repository.specifications.PrestamoSpecifications;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.AuthenticationService;
import com.bibliotech.security.service.UserService;
import com.bibliotech.utils.RoleUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .legajoUsuario(Objects.nonNull(prestamo.getUsuario().getUserInfo()) ? prestamo.getUsuario().getUserInfo().getLegajo() : "")
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

        if (!usuarioAutenticado.isEnabled()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,String.format("El usuario no está habilitado"));
        Role rolActual = usuarioAutenticado.getRoles().stream()
                .filter(rol -> rol.getEndDate() == null)
                .findFirst()
                .orElse(null);
        if (rolActual.equals(RoleUtils.DEFAULT_ROLE_USER)) {
            return usuarioAutenticado;
        } else {
            return userService.findById(prestamoRequest.getUsuarioID()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,String.format("No existe usuario con id %s", prestamoRequest.getUsuarioID())));
        }
    }

    private void verifyFechaPrestamos (Instant fechaInicio, Instant fechaFin, Ejemplar ejemplar) {
        //verificar que fecha fin > fecha inicio
        if(fechaInicio.isAfter(fechaFin)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha inicio debe estar antes de la fecha fin");
        if(Instant.now().isAfter(fechaFin)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha fin debe estar en el futuro");

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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El periodo de tiempo se superpone con prestamos existentes");
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
                                .publicacionId(prestamo.getEjemplar().getPublicacion().getId())
                                .ejemplar(prestamo.getEjemplar() == null ? null : prestamo.getEjemplar().getId())
                                .estado(prestamo.getEstado().size() == 0 ? null : prestamo.getEstado().stream().filter(pe -> pe.getFechaFin() == null).toList().get(0).getEstado().name())
                                .fechaInicio(prestamo.getFechaInicioEstimada())
                                .fechaHasta(prestamo.getFechasRenovaciones().isEmpty() ? prestamo.getFechaFinEstimada() : prestamo.getFechasRenovaciones().get(prestamo.getFechasRenovaciones().toArray().length-1))
                                .build()
                ).toList();
    }

    @Override
    public List<Prestamo> findByParams(PrestamosByParamsRequest request) {

        int parametrosAdmitidos = 0;

        List<Specification<Prestamo>> specificationList = new ArrayList<>();

        Specification<Prestamo> prestamoIdSpec;

        Specification<Prestamo> prestamoEstadoIdSpec;

        Specification<Prestamo> estadoPrestamoNombresSpec;

        Specification<Prestamo> userIdSpec;

        Specification<Prestamo> userDNISpec;

        Specification<Prestamo> userLegajoSpec;

        Specification<Prestamo> userEmailSpec;

        Specification<Prestamo> multaIdSpec;

        Specification<Prestamo> ejemplarIdSpec;

        Specification<Prestamo> publicacionTituloSpec;

        Specification<Prestamo> fechaInicioEstimadaDesdeSpec;

        Specification<Prestamo> fechaInicioEstimadaHastaSpec;

        List<Long> prestamosIds = request.getPrestamosIds();
        if (Objects.nonNull(prestamosIds) && !prestamosIds.isEmpty()) {
            List<Specification<Prestamo>> prestamoIdSpecifications = prestamosIds.stream()
                    .map(PrestamoSpecifications::hasId).collect(Collectors.toList());
            prestamoIdSpec = Specification.anyOf(prestamoIdSpecifications);
            specificationList.add(prestamoIdSpec);
            parametrosAdmitidos++;
        }

        List<Long> userIds = request.getUsuariosIds();
        if (Objects.nonNull(userIds) && !userIds.isEmpty()) {
                List<Specification<Prestamo>> userIdSpecifications = userIds.stream()
              .map(
              PrestamoSpecifications::hasUserWithId).collect(Collectors.toList());
                userIdSpec = Specification.anyOf(userIdSpecifications);
                specificationList.add(userIdSpec);
                parametrosAdmitidos++;
        }

        List<String> userDNIs = request.getUsuariosDNIs();
        if (Objects.nonNull(userDNIs) && !userDNIs.isEmpty()) {
            List<Specification<Prestamo>> userDNISpecifications = userDNIs.stream()
                    .map(
                            PrestamoSpecifications::hasUsuarioWithDNI).collect(Collectors.toList());
            userDNISpec = Specification.anyOf(userDNISpecifications);
            specificationList.add(userDNISpec);
            parametrosAdmitidos++;
        }

        List<String> userLegajos = request.getUsuariosLegajos();
        if (Objects.nonNull(userLegajos) && !userLegajos.isEmpty()) {
            List<Specification<Prestamo>> userLegajoSpecifications = userDNIs.stream()
                    .map(
                            PrestamoSpecifications::hasUsuarioWithLegajo).collect(Collectors.toList());
            userLegajoSpec = Specification.anyOf(userLegajoSpecifications);
            specificationList.add(userLegajoSpec);
            parametrosAdmitidos++;
        }

        List<String> userEmails = request.getUsuariosEmails();
        if (Objects.nonNull(userEmails) && !userEmails.isEmpty()) {
            List<Specification<Prestamo>> userEmailSpecifications = userEmails.stream()
                    .map(
                            PrestamoSpecifications::hasUsuarioWithDNI).collect(Collectors.toList());
            userEmailSpec = Specification.anyOf(userEmailSpecifications);
            specificationList.add(userEmailSpec);
            parametrosAdmitidos++;
        }

        List<Long> ejemplaresIds= request.getEjemplaresIds();
        if (Objects.nonNull(ejemplaresIds) && !ejemplaresIds.isEmpty()) {
            List<Specification<Prestamo>> ejemplarIdSpecifications = ejemplaresIds.stream()
                    .map(
                            PrestamoSpecifications::hasEjemplarWithId).collect(Collectors.toList());
            ejemplarIdSpec = Specification.anyOf(ejemplarIdSpecifications);
            specificationList.add(ejemplarIdSpec);
            parametrosAdmitidos++;
        }

        List<Long> multasIds= request.getMultasIds();
        if (Objects.nonNull(multasIds) && !multasIds.isEmpty()) {
            List<Specification<Prestamo>> multaIdSpecifications = multasIds.stream()
                    .map(
                            PrestamoSpecifications::hasMultaWithId).collect(Collectors.toList());
            multaIdSpec = Specification.anyOf(multaIdSpecifications);
            specificationList.add(multaIdSpec);
            parametrosAdmitidos++;
        }

        List<String> estadoPrestamoNombres = request.getPrestamosEstadosNombres();
        if (Objects.nonNull(estadoPrestamoNombres) && !estadoPrestamoNombres.isEmpty()) {
            List<Specification<Prestamo>> estadoPrestamoNombresEspecifications = estadoPrestamoNombres.stream()
                    .map(
                            PrestamoSpecifications::hasEstadoPrestamoWithNameAndFechaBajaNull).collect(Collectors.toList());
            estadoPrestamoNombresSpec = Specification.anyOf(estadoPrestamoNombresEspecifications);
            specificationList.add(estadoPrestamoNombresSpec);
            parametrosAdmitidos++;
        }

        List<String> publicacionesTitulos = request.getPublicacionesTitulos();
        if (Objects.nonNull(publicacionesTitulos) && !publicacionesTitulos.isEmpty()) {
            List<Specification<Prestamo>> publicacionesTitulosEspecifications = publicacionesTitulos.stream()
                    .map(
                            PrestamoSpecifications::hasPublicacionWithTitulo).collect(Collectors.toList());
            publicacionTituloSpec = Specification.anyOf(publicacionesTitulosEspecifications);
            specificationList.add(publicacionTituloSpec);
            parametrosAdmitidos++;
        }

        List<Long> prestamosEstadosIds = request.getPrestamosEstadosIds();
        if (Objects.nonNull(prestamosEstadosIds) && !prestamosEstadosIds.isEmpty()) {
            List<Specification<Prestamo>> prestamoEstadoIdSpecifications = prestamosEstadosIds.stream()
                    .map(
                            PrestamoSpecifications::hasPrestamoEstadoWithId).collect(Collectors.toList());
            prestamoEstadoIdSpec = Specification.anyOf(prestamoEstadoIdSpecifications);
            specificationList.add(prestamoEstadoIdSpec);
            parametrosAdmitidos++;
        }

        Instant fechaInicioEstimadaDesde = request.getFechaInicioEstimadaDesde();
        if (Objects.nonNull(fechaInicioEstimadaDesde)) {
            fechaInicioEstimadaDesdeSpec = PrestamoSpecifications.fechaInicioEstimadaDesde(fechaInicioEstimadaDesde);
            specificationList.add(fechaInicioEstimadaDesdeSpec);
            parametrosAdmitidos++;
        }

        Instant fechaInicioEstimadaHasta = request.getFechaInicioEstimadaHasta();
        if (Objects.nonNull(fechaInicioEstimadaHasta)) {
            fechaInicioEstimadaHastaSpec = PrestamoSpecifications.fechaInicioEstimadaHasta(fechaInicioEstimadaHasta);
            specificationList.add(fechaInicioEstimadaHastaSpec);
            parametrosAdmitidos++;
        }

        Instant fechaFinEstimadaDesde = request.getFechaFinEstimadaDesde();
        if (Objects.nonNull(fechaFinEstimadaDesde)) {
            fechaInicioEstimadaDesdeSpec = PrestamoSpecifications.fechaFinEstimadaDesde(fechaInicioEstimadaDesde);
            specificationList.add(fechaInicioEstimadaDesdeSpec);
            parametrosAdmitidos++;
        }

        Instant fechaFinEstimadaHasta = request.getFechaFinEstimadaHasta();
        if (Objects.nonNull(fechaFinEstimadaHasta)) {
            fechaInicioEstimadaDesdeSpec = PrestamoSpecifications.fechaFinEstimadaHasta(fechaFinEstimadaHasta);
            specificationList.add(fechaInicioEstimadaDesdeSpec);
            parametrosAdmitidos++;
        }


        if (parametrosAdmitidos > 0) {
                return prestamosRepository.findAll(Specification.allOf(specificationList));
            } else {
                return prestamosRepository.findAll(PrestamoSpecifications.withoutResults());
        }

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
                .estado(Objects.requireNonNull(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null)).getEstado().name())
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
                .estado(Objects.requireNonNull(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null)).getEstado().name())
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
            prestamo.getFechasRenovaciones().add(req.getFechaFinRenovacion());
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
                .estado(Objects.requireNonNull(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null)).getEstado().name())
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
                .estado(Objects.requireNonNull(prestamo.getEstado().stream()
                        .filter(estado -> estado.getFechaFin() == null)
                        .findFirst()
                        .orElse(null)).getEstado().name())
                .build();
    }

}
