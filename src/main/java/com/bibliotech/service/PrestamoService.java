package com.bibliotech.service;
import com.bibliotech.dto.PrestamoRequest;
import com.bibliotech.dto.PrestamoResponse;
import com.bibliotech.entity.Prestamo;

public interface PrestamoService extends BaseService<Prestamo, Long>{
    PrestamoResponse convertDtoToEntity(PrestamoRequest prestamoRequest) throws Exception;
}
