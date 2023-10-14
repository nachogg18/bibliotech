package com.bibliotech.service;

import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;

import java.util.List;

public interface MultaService {
    List<MultaItemTablaDTO> findByParams(FindMultaByParamsDTO multaDTO);
}
