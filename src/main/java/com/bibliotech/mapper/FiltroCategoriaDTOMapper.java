package com.bibliotech.mapper;

import com.bibliotech.dto.FiltroCategoriaDTO;
import com.bibliotech.dto.FiltroCategoriaValorDTO;
import com.bibliotech.entity.Categoria;

import java.util.List;

public class FiltroCategoriaDTOMapper {
    public static List<FiltroCategoriaDTO> toCategoriaDTO(List<Categoria> categoriaList) {
        return categoriaList.stream().map(
                categoria -> {
                    FiltroCategoriaDTO categoriaDTO = new FiltroCategoriaDTO();
                    categoriaDTO.setIdCategoria(categoria.getId());
                    categoria.getCategoriaValorList().forEach(cv -> {
                        FiltroCategoriaValorDTO valorDTO = new FiltroCategoriaValorDTO();
                        valorDTO.setIdValor(cv.getId());
                        valorDTO.setNombreValor(cv.getNombre());
                        categoriaDTO.getValores().add(valorDTO);
                    });
                    return categoriaDTO;
                }
        ).toList();
    }
}
