package com.bibliotech.service;
import com.bibliotech.dto.PrestamoDTO;
import com.bibliotech.entity.Prestamo;

public interface PrestamoService extends BaseService<Prestamo, Long>{
    Prestamo convertDtoToEntity(PrestamoDTO prestamoDTO) throws Exception;
}
