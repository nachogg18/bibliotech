package com.bibliotech.repository;

import com.bibliotech.entity.Comentario;
import com.bibliotech.entity.Ejemplar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EjemplarRepository extends BaseRepository<Ejemplar, Long> {
    Optional<Ejemplar> findById(Long id);

    List<Ejemplar> findByFechaBajaNull();

    List<Ejemplar> findByPublicacionIdAndFechaBajaIsNull(Long publicacion_id);

    @Query("SELECT e.comentarios FROM Ejemplar e WHERE e.id = :ejemplarId")
    List<Comentario> findComentariosByEjemplarId(@Param("ejemplarId") Long ejemplarId);

}
