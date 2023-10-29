package com.bibliotech.service;
import com.bibliotech.dto.*;
import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.dto.PrestamosByParamsRequest;
import com.bibliotech.entity.Prestamo;
import java.util.List;

public interface PrestamoService extends BaseService<Prestamo, Long>{
    PrestamoResponse crearPrestamo(PrestamoRequest prestamoRequest) throws Exception;
    List<FindPrestamoDTO> getPrestamosByUserId(Long idUsuario);
    List<Prestamo> findByParams(PrestamosByParamsRequest request);
    
    PrestamoResponse modifyPrestamo(PrestamoRequest request);

    DetallePrestamoDTO getDetallePrestamo(Long id);

    List<PrestamoItemTablaDTO> getPrestamosListTable();
}
