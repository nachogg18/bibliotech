package com.bibliotech.repository;

import com.bibliotech.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PrestamosRepository extends BaseRepository<Prestamo, Long>, JpaSpecificationExecutor<Prestamo> {
    List<Prestamo> findPrestamoByUsuarioId(Long usuario_id);


    @Query(nativeQuery = true, value = "SELECT publicacionExtend.titulo as 'TÍTULO', " +
            "publicacionExtend.anio as 'AÑO', " +
            "CAST(publicacionExtend.ejemplares AS UNSIGNED) as 'EJEMPLARES', " +
            "edicion.nombre as 'EDICIÓN', " +
            "CAST(COUNT(*) AS UNSIGNED) as 'PRÉSTAMOS' " +
            "FROM prestamo " +
            "LEFT JOIN ejemplar ON prestamo.ejemplar_id = ejemplar.id " +
            "LEFT JOIN ( " +
            "    SELECT publicacion.id, " +
            "           publicacion.titulo, " +
            "           publicacion.anio, " +
            "           publicacion.edicion_id, " +
            "           COUNT(ejemplar.id) as 'ejemplares' " +
            "    FROM publicacion " +
            "    INNER JOIN ejemplar ON publicacion.id = ejemplar.publicacion_id " +
            "    GROUP BY publicacion.id " +
            ") as publicacionExtend ON publicacionExtend.id = ejemplar.publicacion_id " +
            "LEFT JOIN edicion ON publicacionExtend.edicion_id = edicion.id " +
            "WHERE prestamo.fecha_inicio_estimada >= :fechaInicio " +
            "AND prestamo.fecha_inicio_estimada <= :fechaFin " +
            "GROUP BY publicacionExtend.id " +
            "ORDER BY COUNT(*) DESC")
    public List<Object[]> getInformePrestamo(
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );

    @Query(nativeQuery = true, value = "SELECT publicacionExtend.nombre as 'TIPO', " +
            "COUNT(*) as 'CANTIDAD' " +
            "FROM prestamo " +
            "LEFT JOIN ejemplar ON prestamo.ejemplar_id = ejemplar.id " +
            "LEFT JOIN ( " +
            "    SELECT publicacion.id, tipo_publicacion.nombre " +
            "    FROM publicacion " +
            "    INNER JOIN tipo_publicacion ON publicacion.tipo_publicacion_id = tipo_publicacion.id " +
            "    GROUP BY publicacion.id, tipo_publicacion.nombre " +
            ") as publicacionExtend ON publicacionExtend.id = ejemplar.publicacion_id " +
            "WHERE prestamo.fecha_inicio_estimada >= :fechaInicio " +
            "AND prestamo.fecha_inicio_estimada <= :fechaFin " +
            "GROUP BY publicacionExtend.nombre " +
            "ORDER BY COUNT(*) desc")
    List<Object[]> obtenerCantidadPorTipo(
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );

    @Query(nativeQuery = true, value = "SELECT COALESCE(prestamos_renovados.cantidad, 0) AS 'PRESTAMOS RENOVADOS', " +
            "COALESCE(prestamos_no_renovados.cantidad, 0) AS 'PRESTAMOS NO RENOVADOS' " +
            "FROM " +
            "(SELECT COUNT(*) as cantidad FROM prestamo " +
            "WHERE prestamo.fecha_inicio_estimada >= :fechaInicio " +
            "AND prestamo.fecha_inicio_estimada <= :fechaFin " +
            "AND prestamo.id IN (SELECT DISTINCT fechas_renovaciones_prestamo.fecha_renovacion_id FROM fechas_renovaciones_prestamo)) as prestamos_renovados " +
            "LEFT JOIN " +
            "(SELECT COUNT(*) as cantidad FROM prestamo " +
            "WHERE prestamo.fecha_inicio_estimada >= '2023-10-09' " +
            "AND prestamo.fecha_inicio_estimada <= '2023-11-30' " +
            "AND prestamo.id NOT IN (SELECT DISTINCT fechas_renovaciones_prestamo.fecha_renovacion_id FROM fechas_renovaciones_prestamo)) as prestamos_no_renovados ON 1=1")
    Map<String, Long> obtenerCantidadPrestamosSiNoRenovados(
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );


    @Query(nativeQuery = true, value = "SELECT tiempo.diferencia as 'DÍAS', COUNT(*) as 'CANTIDAD' " +
            "FROM (SELECT TIMESTAMPDIFF(DAY, prestamo.fecha_inicio_estimada, prestamo.fecha_fin_estimada) as diferencia " +
            "FROM prestamo WHERE prestamo.fecha_inicio_estimada >= :fechaInicio " +
            "AND prestamo.fecha_inicio_estimada <= :fechaFin) as tiempo " +
            "GROUP BY tiempo.diferencia")
    List<Map<String, Object>> obtenerDiasPrestamo(
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );
}
