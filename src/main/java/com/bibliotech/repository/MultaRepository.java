package com.bibliotech.repository;

import com.bibliotech.entity.Multa;
import com.bibliotech.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long>, JpaSpecificationExecutor<Multa> {

    @Query(nativeQuery = true, value = "SELECT tipo_multa.nombre AS 'TIPO DE MULTA', count(*) AS 'CANTIDAD' " +
            "FROM multa " +
            "LEFT JOIN tipo_multa ON multa.tipo_multa_id = tipo_multa.id " +
            "WHERE multa.fecha_inicio >= :fechaInicio AND multa.fecha_fin <= :fechaFin " +
            "GROUP BY tipo_multa.nombre")
    List<Map<String, Object>> obtenerReporteCantidadMultaPorTipo(
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );

    @Query(nativeQuery = true, value = "SELECT multa.id AS 'ID', " +
            "multa.fecha_inicio AS 'FECHA INICIO', " +
            "multa.fecha_fin AS 'FECHA FIN', " +
            "users.legajo AS 'LEGAJO MULTA', " +
            "tipo_multa.nombre AS 'TIPO MULTA' " +
            "FROM multa " +
            "LEFT JOIN tipo_multa ON multa.tipo_multa_id = tipo_multa.id " +
            "LEFT JOIN users ON users.id = multa.user_id " +
            "WHERE multa.fecha_inicio >= :fechaInicio " +
            "AND multa.fecha_fin <= :fechaFin")
    List<Map<String, Object>> obtenerReporteDetallesMulta(
            @Param("fechaInicio") String fechaInicio,
            @Param("fechaFin") String fechaFin
    );

    List<Multa> findByUserId(Long user_id);

    @Query( "SELECT m FROM Multa m "+
            "WHERE m.user = :user "+
            "AND EXISTS( "+
                "SELECT 1 FROM m.multaEstados me "+
                "WHERE me.fechaFin IS NULL "+
                "AND me.estadoMulta = com.bibliotech.entity.EstadoMulta.ACTIVA "+
            ")"
    )
    Optional<Multa> obtenerMultaActivaByUser(@Param("user") User user);

    @Query(" SELECT m FROM Multa m WHERE m.user.id = :userID ")
    List<Multa> obtenerMultasByUserId(@Param("userID") Long userID);
}
