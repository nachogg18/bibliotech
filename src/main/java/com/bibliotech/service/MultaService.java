package com.bibliotech.service;

import com.bibliotech.dto.CreateMultaDTO;
import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaDetalleDTO;
import com.bibliotech.dto.MultaItemTablaDTO;
import com.bibliotech.entity.Multa;
import com.bibliotech.dto.MultaResponse;

import java.util.List;

public interface MultaService {
    List<MultaItemTablaDTO> findByParams(FindMultaByParamsDTO multaDTO);
    //boolean createMulta(CreateMultaDTO request) throws Exception;

    List<MultaItemTablaDTO> getMultasByUserId(Long idUsuario);

    boolean deleteMulta(Long id);

    MultaDetalleDTO getMultaDetalle(Long id);

    boolean updateMulta(CreateMultaDTO request, Long id);

    boolean isUsuarioHabilitado(Long id);

    boolean createMulta(CreateMultaDTO request) throws Exception;

    MultaResponse finalizarMulta (Long id);

    MultaResponse cancelarMulta (Long id);
}
