package com.bibliotech.service;

import com.bibliotech.dto.PrestamoRequest;
import com.bibliotech.dto.PrestamoResponse;
import com.bibliotech.entity.*;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
import com.bibliotech.security.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
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
    public PrestamoResponse convertDtoToEntity(PrestamoRequest prestamoRequest) throws Exception{
        //verificar que usuario y ejemplar existen
        if (!userService.exists(prestamoRequest.getUsuarioID())) throw new RuntimeException(String.format("No existe usuario con id %s", prestamoRequest.getUsuarioID()));
        if (!ejemplarExistsAndEstado(prestamoRequest)) throw new RuntimeException(String.format("No existe ejemplar con id %s", prestamoRequest.getEjemplarID()));
        Prestamo prestamo = Prestamo.builder()
                .estado(new ArrayList<>())
                .fechaAlta(Instant.now())
                .fechaInicioEstimada(prestamoRequest.getFechaInicioEstimada())
                .fechaFinEstimada(prestamoRequest.getFechaFinEstimada())
                .usuario(userService.findById(prestamoRequest.getUsuarioID()).get())
                .ejemplar(ejemplarService.findById(prestamoRequest.getEjemplarID()).get())
                .build();
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.ACTIVO);
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

    private boolean ejemplarExistsAndEstado (PrestamoRequest prestamoRequest){
        if (ejemplarService.exists(prestamoRequest.getUsuarioID())) {
            Ejemplar ejemplar = ejemplarService.findById(prestamoRequest.getEjemplarID()).get();
            Optional<EjemplarEstado> ultimoEstado = ejemplar.getEjemplarEstadoList().stream()
                    .max((obj1, obj2) -> obj1.getEstadoEjemplar().compareTo(obj2.getEstadoEjemplar()));
            return (ultimoEstado.get().getEstadoEjemplar() == EstadoEjemplar.DISPONIBLE );
        } else {
            return false;
        }
    }
}
