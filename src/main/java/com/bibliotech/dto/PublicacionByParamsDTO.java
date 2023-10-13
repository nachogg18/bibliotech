package com.bibliotech.dto;

import com.bibliotech.entity.*;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PublicacionByParamsDTO {
    private Long id;
    private int anioPublicacion;
    private String isbnPublicacion;
    private String tipoPublicacion;
    private String tituloPublicacion;
    private int nroPaginas;
    private List<Autor> autores;
    private Edicion edicion;
    private LinkDTO link;
    private List<CategoriaPublicacion> categorias;
    private TipoPublicacion tipo;
    private List<Editorial> editoriales;
}

