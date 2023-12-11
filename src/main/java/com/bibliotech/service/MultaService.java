package com.bibliotech.service;

import com.bibliotech.dto.CreateMultaDTO;
import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;

import java.util.List;

public interface MultaService {
    List<MultaItemTablaDTO> findByParams(FindMultaByParamsDTO multaDTO);
    boolean createMulta(CreateMultaDTO request) throws Exception;

    List<MultaItemTablaDTO> getMultasByUserId(Long idUsuario);

    boolean isUsuarioHabilitado(Long id);
}
