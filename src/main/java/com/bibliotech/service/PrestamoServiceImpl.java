package com.bibliotech.service;

import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.dto.PrestamoRequest;
import com.bibliotech.dto.PrestamoResponse;
import com.bibliotech.entity.*;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.security.entity.Role;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

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
    public PrestamoServiceImpl (BaseRepository<Prestamo, Long> baseRepository, UserService userService) {
        super(baseRepository);
    }

    @Override
    @Transactional
    public PrestamoResponse crearPrestamo(PrestamoRequest prestamoRequest) {

        if(prestamoRequest.getFechaInicioEstimada().isAfter(prestamoRequest.getFechaFinEstimada())) throw new ValidationException("La fecha inicio debe estar antes de la fecha fin");
        verifyUsuarioYEjemplar(prestamoRequest);
        verifyFechaPrestamos(prestamoRequest);

        Prestamo prestamo = Prestamo.builder()
                .estado(new ArrayList<>())
                .fechaAlta(Instant.now())
                .fechaInicioEstimada(prestamoRequest.getFechaInicioEstimada())
                .fechaFinEstimada(prestamoRequest.getFechaFinEstimada())
                .usuario(userService.findById(prestamoRequest.getUsuarioID()).get())
                .ejemplar(ejemplarService.findById(prestamoRequest.getEjemplarID()).get())
                .build();

        PrestamoEstado prestamoEstado = new PrestamoEstado();

        if (prestamoRequest.getFechaInicioEstimada().isBefore(Instant.now()) || prestamoRequest.getFechaInicioEstimada().equals(Instant.now())) {
            prestamoEstado.setEstado(EstadoPrestamo.ACTIVO);
        } else {
            prestamoEstado.setEstado(EstadoPrestamo.EN_ESPERA);
        }

        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);
        prestamosRepository.save(prestamo);

        return PrestamoResponse
                .builder()
                .usuarioID(userService.findById(prestamoRequest.getUsuarioID()).get().getId())
                .ejemplarID(ejemplarService.findById(prestamoRequest.getEjemplarID()).get().getId())
                .PrestamoID(prestamo.getId())
                .fechaInicioEstimada(prestamo.getFechaInicioEstimada())
                .fechaAlta(prestamo.getFechaAlta())
                .build();
    }

    private void verifyUsuarioYEjemplar (PrestamoRequest prestamoRequest){
        //usuario existente, rol correspondiente y habilitado
        User usuario = userService.findById(prestamoRequest.getUsuarioID()).orElseThrow(() -> new ValidationException(String.format("No existe usuario con el id %s", prestamoRequest.getUsuarioID())));
        if (!usuario.isEnabled()) throw new ValidationException(String.format("El usuario no está habilitado"));
//        Role rolActual = usuario.getRoles().stream()
//                .filter(rol -> rol.getEndDate() == null)
//                .findFirst()
//                .orElse(null);
//      if (rolActual.) TODO: check rol usuario

        //ejemplar existente y disponible
        if (ejemplarService.exists(prestamoRequest.getEjemplarID())){
        Ejemplar ejemplar = ejemplarService.findById(prestamoRequest.getEjemplarID()).get();
        EjemplarEstado ultimoEstado = ejemplar.getEjemplarEstadoList().stream()
                .filter(estado -> estado.getFechaFin() == null)
                .findFirst()
                .orElse(null);
        if (ultimoEstado.getEstadoEjemplar() == EstadoEjemplar.EN_REPARACION || ultimoEstado.getEstadoEjemplar() == EstadoEjemplar.EXTRAVIADO) throw new ValidationException(String.format("El ejemplar con el id %s no está disponible", prestamoRequest.getEjemplarID()));
        } else {
            throw new ValidationException(String.format("No existe ejemplar con id %s", prestamoRequest.getEjemplarID()));
        }
    }

    private void verifyFechaPrestamos (PrestamoRequest prestamoRequest) {
        //comparar con dias maximos y minimos parametrizados
        // if(prestamoRequest.getFechaInicioEstimada().until(prestamoRequest.getFechaFinEstimada(), ChronoUnit.DAYS) > días max parametrizados) throw new ValidationException(String.format("El periodo de tiempo supera el permitido"))
        // if(prestamoRequest.getFechaInicioEstimada().until(prestamoRequest.getFechaFinEstimada(), ChronoUnit.DAYS) < días min parametrizados) throw new ValidationException(String.format("El periodo de tiempo supera el permitido"))


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
}
