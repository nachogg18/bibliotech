package com.bibliotech.service;

import com.bibliotech.dto.PublicacionResponseDTO;
import com.bibliotech.entity.Publicacion;
import com.bibliotech.mapper.PublicacionRequestMapper;
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
        return PublicacionRequestMapper.toResponseDTO(publicaciones);
    }

}
