package com.bibliotech.service;

import com.bibliotech.dto.PrestamoRequest;
import com.bibliotech.dto.PrestamoResponse;
import com.bibliotech.entity.*;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.security.entity.User;
import com.bibliotech.security.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

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
        PrestamoEstado prestamoEstado = PrestamoEstado.builder().estado(EstadoPrestamo.ACTIVO).build();
        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);

        prestamosRepository.save(prestamo);

        PrestamoResponse response = PrestamoResponse
                .builder()
                .UsuarioID(userService.findById(prestamoRequest.getUsuarioID()).get().getId())
                .EjemplarID(ejemplarService.findById(prestamoRequest.getEjemplarID()).get().getId())
                .PrestamoID(prestamo.getId())
                .fechaInicioEstimada(prestamo.getFechaInicioEstimada())
                .fechaAlta(prestamo.getFechaAlta())
                .build();
        return response;
    }

    private void verifyUsuarioYEjemplar (PrestamoRequest prestamoRequest){
        //usuario existente, rol correspondiente y habilitado
        if (!userService.exists(prestamoRequest.getUsuarioID())) throw new ValidationException(String.format("No existe usuario con id %s", prestamoRequest.getUsuarioID()));

        //userService.findById(prestamoRequest.getUsuarioID()).get().getRoles().;


        //ejemplar existente y disponible
        if (!ejemplarService.exists(prestamoRequest.getEjemplarID())) throw new ValidationException(String.format("No existe ejemplar con id %s", prestamoRequest.getEjemplarID()));
        Ejemplar ejemplar = ejemplarService.findById(prestamoRequest.getEjemplarID()).get();
        Optional<EjemplarEstado> ultimoEstado = ejemplar.getEjemplarEstadoList().stream()
                .max(Comparator.comparing(EjemplarEstado::getEstadoEjemplar));
        if (ultimoEstado.get().getEstadoEjemplar() == EstadoEjemplar.EN_REPARACION || ultimoEstado.get().getEstadoEjemplar() == EstadoEjemplar.EXTRAVIADO) throw new ValidationException(String.format("El ejemplar con el id %s no está disponible", prestamoRequest.getEjemplarID()));
    }

    private void verifyFechaPrestamos (PrestamoRequest prestamoRequest) {
        //comparar con dias maximos parametrizados
        // if(prestamoRequest.getFechaInicioEstimada().until(prestamoRequest.getFechaFinEstimada(), ChronoUnit.DAYS) < dias parametrizados) throw new ValidationException(String.format("El periodo de tiempo supera el permitido"))

        Ejemplar ejemplar = ejemplarService.findById(prestamoRequest.getEjemplarID()).get();
        boolean prestamoOverlap = ejemplar.getPrestamos().stream()
                .noneMatch(prestamo ->
                        !prestamo.overlapsWith(prestamoRequest.getFechaInicioEstimada(), prestamoRequest.getFechaFinEstimada())
                );

        if(prestamoOverlap) throw new ValidationException(String.format("El periodo de tiempo se superpone con prestamos existentes", prestamoRequest.getEjemplarID()));

    }

    private void crearPrestamoActual (PrestamoRequest prestamoRequest) {
        //si no hay ningún préstamo pedido

    }
}
