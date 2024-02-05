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

    @Query("SELECT e FROM Ejemplar e WHERE e.ubicacion.id = :id")
    Optional<Ejemplar> findEjemplarByUbicacionId(Long id);

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

    @Query(value = "SELECT ej.id as id_ejemplar, " +
            "       COALESCE(AVG(com.calificacion), 10) as valoracion, " +
            "       ub.descripcion as ubicacion, " +
            "       bi.nombre as biblioteca, " +
            "       p.id as id_publicacion, " +
            "       p.titulo, " +
            "       p.sinopsis, " +
            "       p.anio, " +
            "       tp.nombre as tipo, " +
            "       p.isbn, " +
            "       GROUP_CONCAT(DISTINCT ed.nombre SEPARATOR ';') AS editoriales, " +
            "       GROUP_CONCAT(DISTINCT CONCAT(a.nombre, ' ', a.apellido) SEPARATOR ';') as autores, " +
            "       e.nombre " +
            "FROM ejemplar ej " +
            "LEFT JOIN ejemplar_comentario ejc ON ej.id = ejc.ejemplar_id " +
            "LEFT JOIN comentario com ON com.id = ejc.comentario_id " +
            "LEFT JOIN ubicacion ub ON ej.ubicacion_id = ub.id " +
            "LEFT JOIN biblioteca bi ON bi.id = ub.biblioteca_id " +
            "LEFT JOIN publicacion p ON ej.publicacion_id = p.id " +
            "LEFT JOIN edicion ed ON p.edicion_id = ed.id " +
            "LEFT JOIN tipo_publicacion tp ON p.tipo_publicacion_id = tp.id " +
            "LEFT JOIN publicacion_editorial pe ON pe.publicacion_id = p.id " +
            "LEFT JOIN editorial e ON e.id = pe.editorial_id " +
            "LEFT JOIN publicacion_autor pa ON pa.publicacion_id = p.id " +
            "LEFT JOIN autor a ON a.id = pa.autor_id " +
            "WHERE ej.serialnfc = :serialNfc " +
            "GROUP BY ej.id, p.id, p.sinopsis, p.titulo, p.anio, tp.nombre, p.isbn, e.nombre, ub.descripcion, bi.nombre",
            nativeQuery = true)
    List<Object[]> obtenerEjemplarNFC(@Param("serialNfc") String serialNfc);

}
