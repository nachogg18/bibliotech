package com.bibliotech.service;
import com.bibliotech.dto.*;
import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.dto.PrestamosByParamsRequest;
import com.bibliotech.entity.Prestamo;
import com.bibliotech.entity.PrestamoEstado;

import java.time.Instant;
import java.util.List;

public interface PrestamoService extends BaseService<Prestamo, Long>{
    PrestamoResponse crearPrestamo(PrestamoRequest prestamoRequest) throws Exception;

    List<FindPrestamoDTO> getPrestamosByUserId(Long idUsuario);

    List<PrestamoItemTablaDTO> getPrestamosListTable();

    List<Prestamo> findByParams(PrestamosByParamsRequest request);

    List<PrestamoSearchItemTablaDTO> searchPrestamos(PrestamoSearchDTO request);

    DetallePrestamoDTO getDetallePrestamo(Long id);

    PrestamoResponse checkOutPrestamo(Long id);

    PrestamoResponse checkInPrestamo(Long id);

    PrestamoResponse cancelarPrestamo(Long id);

    PrestamoResponse renovarPrestamo(Long id, RenovacionDTO req);

    PrestamoResponse extravioPrestamo(Long id);

    List<PrestamoEstado> getHistorial(Long id);

    List<Instant> getRenovaciones(Long id);

    List<Prestamo> findAllByFechaBajaNullAndFechaFinEstimadaBefore(Instant now);

    List<Prestamo> findAllByIntervalFechaFinAndFechaBajaNull(Instant inicio, Instant fin);
}
