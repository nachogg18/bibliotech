package com.bibliotech.service;
import com.bibliotech.dto.FindPrestamoDTO;
import com.bibliotech.dto.PrestamoDTO;
import com.bibliotech.entity.Prestamo;

import java.util.List;

public interface PrestamoService extends BaseService<Prestamo, Long>{
    Prestamo convertDtoToEntity(PrestamoDTO prestamoDTO) throws Exception;
    List<FindPrestamoDTO> getPrestamosByUserId(Long idUsuario);
}
