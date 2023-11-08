package com.bibliotech.repository;

import com.bibliotech.entity.Comentario;
import com.bibliotech.entity.Ejemplar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface EjemplarRepository extends BaseRepository<Ejemplar, Long> {
    Optional<Ejemplar> findById(Long id);

    List<Ejemplar> findByFechaBajaNull();

    List<Ejemplar> findByPublicacionIdAndFechaBajaIsNull(Long publicacion_id);

    @Query("SELECT e.comentarios FROM Ejemplar e WHERE e.id = :ejemplarId")
    List<Comentario> findComentariosByEjemplarId(@Param("ejemplarId") Long ejemplarId);

    @Query(nativeQuery = true, value = "SELECT ejemplar.id as 'ID EJEMPLAR', " +
            "ejemplar.serialnfc as 'NCF SERIAL', " +
            "publicacion.titulo as 'PUBLICACION', " +
            "ejemplar_estado.estado_ejemplar as 'ESTADO' " +
            "FROM ejemplar " +
            "LEFT JOIN publicacion ON publicacion.id = ejemplar.publicacion_id " +
            "LEFT JOIN ejemplar_ejemplar_estado_list ON ejemplar.id = ejemplar_ejemplar_estado_list.ejemplar_id " +
            "LEFT JOIN ejemplar_estado ON ejemplar_estado.id = ejemplar_ejemplar_estado_list.ejemplar_estado_list_id " +
            "WHERE ejemplar.fecha_baja IS NULL AND ejemplar_estado.fecha_fin IS NULL")
    List<Map<String, Object>> obtenerReporteExistencias();

}
