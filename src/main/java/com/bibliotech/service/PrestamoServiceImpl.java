package com.bibliotech.service;

import com.bibliotech.dto.*;
import com.bibliotech.entity.*;
import com.bibliotech.repository.BaseRepository;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServiceImpl extends BaseServiceImpl<Prestamo, Long> implements PrestamoService{
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(PrestamoServiceImpl.class);
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
        verifyFechaPrestamos(prestamoRequest);

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
        if (prestamoRequest.getFechaInicioEstimada().isBefore(Instant.now()) || prestamoRequest.getFechaInicioEstimada().equals(Instant.now())) {
            prestamoEstado.setEstado(EstadoPrestamo.ACTIVO);
        } else {
            prestamoEstado.setEstado(EstadoPrestamo.EN_ESPERA);
        }

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

    private void verifyFechaPrestamos (PrestamoRequest prestamoRequest) {
        //verificar que fecha fin > fecha inicio
        if(prestamoRequest.getFechaInicioEstimada().isAfter(prestamoRequest.getFechaFinEstimada())) throw new ValidationException("La fecha inicio debe estar antes de la fecha fin");

        //comparar con dias maximos y minimos parametrizados
        // if(prestamoRequest.getFechaInicioEstimada().until(prestamoRequest.getFechaFinEstimada(), ChronoUnit.DAYS) > días max parametrizados) throw new ValidationException(String.format("El periodo de tiempo supera el permitido"))
        // if(prestamoRequest.getFechaInicioEstimada().until(prestamoRequest.getFechaFinEstimada(), ChronoUnit.DAYS) < días min parametrizados) throw new ValidationException(String.format("El periodo de tiempo supera el permitido"))

        //comparar overlap con otras fechas
        Ejemplar ejemplar = ejemplarService.findById(prestamoRequest.getEjemplarID()).get();
        if (!ejemplar.getPrestamos().isEmpty()) {
            boolean prestamoOverlap = ejemplar.getPrestamos().stream()
                    .anyMatch(subEntity -> subEntity.overlapsWith(prestamoRequest.getFechaInicioEstimada(), prestamoRequest.getFechaFinEstimada()));
            if (prestamoOverlap)
                throw new ValidationException(String.format("El periodo de tiempo se superpone con prestamos existentes"));
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
    public List<Prestamo> findByParams(PrestamosByParamsRequest request) {

        int parametrosAdmitidos = 0;

        List<Specification<Prestamo>> specificationList = new ArrayList<>();

        Specification<Prestamo> userIdSpec;

        Specification<Prestamo> fechaInicioEstimadaDesdeSpec;

        Specification<Prestamo> fechaInicioEstimadaHastaSpec;

        List<Long> userIds= request.getUsuariosIds();
            if (Objects.nonNull(userIds) && !userIds.isEmpty()) {
                List<Specification<Prestamo>> userIdSpecifications = userIds.stream()
              .map(
              PrestamoSpecifications::hasUserWithId).collect(Collectors.toList());
                userIdSpec = Specification.anyOf(userIdSpecifications);
                specificationList.add(userIdSpec);
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
    public PrestamoResponse modifyPrestamo(PrestamoRequest request) {


        return null;
    }

}
