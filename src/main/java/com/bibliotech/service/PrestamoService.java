package com.bibliotech.service;
import com.bibliotech.dto.*;
import com.bibliotech.entity.Prestamo;

import java.util.List;

public interface PrestamoService extends BaseService<Prestamo, Long>{
    PrestamoResponse crearPrestamo(PrestamoRequest prestamoRequest) throws Exception;
    List<FindPrestamoDTO> getPrestamosByUserId(Long idUsuario);

    PrestamoResponse checkOutPrestamo(Long id);

    DetallePrestamoDTO getDetallePrestamo(Long id);

    List<PrestamoItemTablaDTO> getPrestamosListTable();

    PrestamoResponse checkInPrestamo(Long id);

    PrestamoResponse cancelarPrestamo(Long id);

    PrestamoResponse renovarPrestamo(Long id, RenovacionDTO req);

    PrestamoResponse extravioPrestamo(Long id);
}
