package com.bibliotech.service;

import com.bibliotech.dto.PublicacionResponseDTO;
import com.bibliotech.entity.Publicacion;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PublicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicacionServiceImpl extends BaseServiceImpl<Publicacion, Long> implements PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    public PublicacionServiceImpl(BaseRepository<Publicacion, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public List<PublicacionResponseDTO> findAllPublicacionDTO() {
        List<Publicacion> publicaciones = publicacionRepository.findAll();
        return publicaciones.stream().map(
                publicacion -> {
                    PublicacionResponseDTO dto = new PublicacionResponseDTO();
                    dto.setId(publicacion.getId());
                    dto.setTituloPublicacion(publicacion.getTitulo());
                    publicacion.getAutores().forEach(a -> dto.getNombreAutores().add(a.getApellido().toUpperCase() + ", " + a.getNombre()));
                    dto.setNombreEditorial(publicacion.getEditoriales().get(0).getNombre()); // TODO: Tomar la ultima editorial
                    dto.setAnioPublicacion(publicacion.getAnio());
                    dto.setNombreEdicion(publicacion.getEdicion().getNombre());
                    return dto;
                }
        ).toList();
    }

}
