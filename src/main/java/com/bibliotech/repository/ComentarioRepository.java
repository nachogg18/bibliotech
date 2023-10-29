package com.bibliotech.repository;

import com.bibliotech.entity.Comentario;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends BaseRepository<Comentario, Long> {
    Optional<Comentario> findById(Long id);

    List<Comentario> findByFechaBajaNull();

}
