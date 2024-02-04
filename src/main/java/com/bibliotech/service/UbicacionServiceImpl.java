package com.bibliotech.service;

import com.bibliotech.dto.UbicacionDTO;
import com.bibliotech.entity.Ubicacion;
import com.bibliotech.repository.UbicacionRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final BibliotecaService bibliotecaService;

    @Override
    public List<Ubicacion> findAll() {
        return ubicacionRepository.findByFechaBajaNull();
    }

    @Override
    public List<Ubicacion> findAllDisponibles() {
        return ubicacionRepository.findByFechaBajaNullAndOcupadaFalse();
    }

    @Override
    public List<Ubicacion> findAllDispoblesWith(Long id) {
        List<Ubicacion> ubicaciones = findAllDisponibles();
        Optional<Ubicacion> ubicacionEjemplar = findById(id);
        if(ubicacionEjemplar.isPresent()){
            ubicaciones.add(ubicacionEjemplar.get());
            return ubicaciones;
        }
        return ubicaciones;
    }

    @Override
    public Optional<Ubicacion> findById(Long id) {
        return ubicacionRepository.findById(id);
    }

    @Override
    public Ubicacion save(UbicacionDTO ubicacion) {
        Ubicacion u = new Ubicacion();
        u.setOcupada(false);
        u.setBiblioteca(bibliotecaService.findOne(ubicacion.getBibliotecaId()));
        u.setDescripcion(ubicacion.getDescripcion());
        return ubicacionRepository.save(u);
    }

    @Override
    public Ubicacion edit(Ubicacion ubicacion, Long id) {
        if (ubicacionRepository.findById(id).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        ubicacion.setId(id);
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public Ubicacion changeOcupada(Long id, boolean state) {
       Optional<Ubicacion> ubicacion =  ubicacionRepository.findById(id);
        if (ubicacion.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        ubicacion.get().setOcupada(state);
        return ubicacionRepository.save(ubicacion.get());
    }

    @Override
    public Optional<Ubicacion> delete(Long id) {
        Optional<Ubicacion> ubicacionOptional = ubicacionRepository.findById(id);
        if (ubicacionOptional.isPresent()) {
            Ubicacion ubicacion = ubicacionOptional.get();
            if (ubicacion.getFechaBaja() != null)
                ubicacionOptional = Optional.empty();
            else {
                ubicacion.setId(id);
                ubicacion.setFechaBaja(Instant.now());
                ubicacionOptional = Optional.of(ubicacionRepository.save(ubicacion));
            }
        }
        return ubicacionOptional;
    }

    @Override
    public Ubicacion saveChanges(Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    @Override
    public Long count() {
        return ubicacionRepository.count();
    }
}
