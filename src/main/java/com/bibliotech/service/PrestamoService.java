package com.bibliotech.service;
import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.dto.PrestamoDTO;
import com.bibliotech.dto.PrestamoRequest;
import com.bibliotech.dto.PrestamoResponse;
import com.bibliotech.entity.Prestamo;

import java.util.List;

public interface PrestamoService extends BaseService<Prestamo, Long>{
    PrestamoResponse crearPrestamo(PrestamoRequest prestamoRequest) throws Exception;
    List<FindPrestamoDTO> getPrestamosByUserId(Long idUsuario);
}
