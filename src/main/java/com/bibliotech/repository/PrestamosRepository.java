package com.bibliotech.repository;

import com.bibliotech.dto.PrestamoSearchItemTablaDTO;
import com.bibliotech.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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
            "WHERE prestamo.fecha_inicio_estimada >= :fechaInicio " +
            "AND prestamo.fecha_inicio_estimada <= :fechaFin " +
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



    @Query("SELECT NEW com.bibliotech.dto.PrestamoSearchItemTablaDTO(p.id, pu.titulo, ej.id, us.id, p.fechaInicioEstimada, p.fechaFinEstimada, pe.estado) " +
            "FROM Prestamo p " +
            "LEFT JOIN p.ejemplar ej " +
            "LEFT JOIN ej.publicacion pu " +
            "LEFT JOIN p.usuario us " +
            "LEFT JOIN us.userInfo usi " +
            "LEFT JOIN p.estado pe " +
            "WHERE (:dni IS NULL OR usi.dni = :dni) " +
            "AND (:titulo IS NULL OR pu.titulo LIKE %:titulo%) " +
            "AND (:fechaInicioEstimada IS NULL OR p.fechaInicioEstimada = :fechaInicioEstimada) " +
            "AND (:fechaFinEstimada IS NULL OR p.fechaFinEstimada = :fechaFinEstimada)" +
            "AND pe.fechaFin IS NULL")
    List<PrestamoSearchItemTablaDTO> findPrestamosByFilters(
            @Param("dni") String dni,
            @Param("titulo") String titulo,
            @Param("fechaInicioEstimada") Instant fechaInicioEstimada,
            @Param("fechaFinEstimada") Instant fechaFinEstimada
    );

    List<Prestamo> findAllByFechaBajaNullAndFechaFinEstimadaBefore(Instant currentInstant);

    @Query("SELECT p FROM Prestamo p WHERE p.fechaBaja IS NULL AND p.fechaFinEstimada BETWEEN :startInstant AND :endInstant")
    List<Prestamo> findAllByIntervalFechaFinAndFechaBajaNull(
            @Param("startInstant") Instant startInstant,
            @Param("endInstant") Instant endInstant
    );
}
