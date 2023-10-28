package com.bibliotech.service;

import com.bibliotech.controller.BaseControllerImpl;
import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.dto.PrestamoDTO;
import com.bibliotech.dto.PrestamosByParamsRequest;
import com.bibliotech.entity.EstadoPrestamo;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.entity.PrestamoEstado;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.repository.specifications.PrestamoSpecifications;
import com.bibliotech.security.service.UserService;
import jakarta.transaction.Transactional;
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
    private static final Logger logger = LoggerFactory.getLogger(BaseControllerImpl.class);
    @Autowired
    private PrestamosRepository prestamosRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EjemplarService ejemplarService;
    @Autowired
    private PrestamoEstadoRepository prestamoEstadoRepository;
    public PrestamoServiceImpl (BaseRepository<Prestamo, Long> baseRepository, UserService userService) {
        super(baseRepository);
    }
    @Override
    @Transactional
    public Prestamo convertDtoToEntity(PrestamoDTO prestamoDTO) throws Exception {
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(userService.findById(prestamoDTO.getUsuarioID()).get());
        prestamo.setEjemplar(ejemplarService.findById(prestamoDTO.getEjemplarID()).get());
        prestamo.setFechaBaja(prestamoDTO.getFechaFinEstimada());
        prestamo.setFechaInicioEstimada(prestamoDTO.getFechaInicioEstimada());
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.ACTIVO);
        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);
        return prestamosRepository.save(prestamo);
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

}
