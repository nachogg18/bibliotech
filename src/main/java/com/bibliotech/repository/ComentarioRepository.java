package com.bibliotech.repository;

import com.bibliotech.entity.Comentario;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends BaseRepository<Comentario, Long> {
    Optional<Comentario> findById(Long id);

    List<Comentario> findByFechaBajaNull();

    List<Comentario> findByAltaUserId(Long id);

    @Query(value = "SELECT c.id as id_comentario, " +
            "       p.titulo as publicacion, " +
            "       c.fecha_alta as fecha_creacion, " +
            "       c.calificacion as calificacion, " +
            "       c.comentario as comentario " +
            "FROM comentario c " +
            "LEFT JOIN publicacion_comentario oc ON c.id = oc.comentario_id " +
            "LEFT JOIN publicacion p ON p.id = oc.publicacion_id " +
            "WHERE c.alta_user_id = :userId AND p.titulo IS NOT NULL AND c.fecha_baja IS NULL", nativeQuery = true)
    List<Object[]> findComentariosPublicacionByUserId(@Param("userId") Long userId);


    @Query(value = "SELECT c.id as id_comentario, " +
            "       ej.id as ejemplar_id, " +
            "       p.titulo as publicacion, " +
            "       c.fecha_alta as fecha_creacion, " +
            "       c.calificacion as calificacion, " +
            "       c.comentario as comentario " +
            "FROM comentario c " +
            "LEFT JOIN ejemplar_comentario ec ON c.id = ec.comentario_id " +
            "LEFT JOIN ejemplar ej ON ej.id = ec.ejemplar_id " +
            "LEFT JOIN publicacion p ON p.id = ej.publicacion_id " +
            "WHERE c.alta_user_id = :userId AND ej.id IS NOT NULL AND c.fecha_baja IS NULL", nativeQuery = true)
    List<Object[]> findComentariosEjemplarByUserId(@Param("userId") Long userId);

}
