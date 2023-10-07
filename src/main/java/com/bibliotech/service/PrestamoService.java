package com.bibliotech.service;
import com.bibliotech.dto.PrestamoRequest;
import com.bibliotech.entity.Prestamo;

public interface PrestamoService extends BaseService<Prestamo, Long>{
    Object convertDtoToEntity(PrestamoRequest prestamoRequest) throws Exception;
}
