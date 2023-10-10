package com.bibliotech.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModificarPublicacionDTO {
    private Integer anio;
    private String isbn;
    private String titulo;
    private Integer nroPaginas;
    private Long edicionID;
    private Long linkID;
    private List<Long> categoriasIDs;
    private List<Long> autoresIDs;
    private List<Long> editorialesIDs;
    private Long tipoPublicacionID;
}
