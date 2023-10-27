package com.bibliotech.service;

import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.dto.PrestamoDTO;
import com.bibliotech.dto.PrestamoItemTablaDTO;
import com.bibliotech.entity.EstadoPrestamo;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.entity.PrestamoEstado;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.security.service.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl extends BaseServiceImpl<Prestamo, Long> implements PrestamoService{
    @Autowired
    private ModelMapper modelMapper;
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
}