package com.bibliotech.service;

import com.bibliotech.dto.FindMultaByParamsDTO;
import com.bibliotech.dto.MultaItemTablaDTO;
import com.bibliotech.entity.Multa;

import java.util.List;

public interface MultaService {
    List<Multa> findAll();
    List<MultaItemTablaDTO> findByParams(FindMultaByParamsDTO multaDTO);
}
