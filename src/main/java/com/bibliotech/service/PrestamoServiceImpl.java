package com.bibliotech.service;

import com.bibliotech.dto.PrestamoDTO;
import com.bibliotech.entity.EstadoPrestamo;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.entity.PrestamoEstado;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PrestamoEstadoRepository;
import com.bibliotech.repository.PrestamosRepository;
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
    private UsuarioService usuarioService;
    @Autowired
    private EjemplarService ejemplarService;
    @Autowired
    private PrestamoEstadoRepository prestamoEstadoRepository;
    public PrestamoServiceImpl (BaseRepository<Prestamo, Long> baseRepository, UsuarioService usuarioService) {
        super(baseRepository);
    }
    @Override
    public Prestamo convertDtoToEntity(PrestamoDTO prestamoDTO) throws Exception {
//        modelMapper.getConfiguration()
//                .setMatchingStrategy(MatchingStrategies.LOOSE);
//        Prestamo prestamo = modelMapper.map(prestamoDTO, Prestamo.class);
//        return prestamo;
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuarioService.findById(prestamoDTO.getUsuarioID()));
        prestamo.setEjemplar(ejemplarService.findById(prestamoDTO.getEjemplarID()));
        prestamo.setFechaBaja(prestamoDTO.getFechaFinEstimada());
        prestamo.setFechaInicioEstimada(prestamoDTO.getFechaInicioEstimada());
        PrestamoEstado prestamoEstado = new PrestamoEstado();
        prestamoEstado.setEstado(EstadoPrestamo.ACTIVO);
        prestamoEstadoRepository.save(prestamoEstado);
        prestamo.getEstado().add(prestamoEstado);
        return prestamosRepository.save(prestamo);
    }
}
