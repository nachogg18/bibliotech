package com.bibliotech.service;

import com.bibliotech.dto.PrestamoDTO;
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
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.LOOSE);
//        Prestamo prestamo = modelMapper.map(prestamoDTO, Prestamo.class);
//        return prestamo;
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
}
