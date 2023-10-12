package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;

import com.bibliotech.entity.TipoPublicacion;
import lombok.Data;

@Data
public class DetallePublicacionDTO {
    private Long id;
    private String tituloPublicacion;
    private String isbnPublicacion;
    private Integer anioPublicacion;
    private Integer nroPaginas;
    private List<AutorDTO> autores = new ArrayList<>();
    private EdicionDTO edicion;
    private List<EditorialDTO> editoriales = new ArrayList<>();
    private List<DetalleCategoriaDTO> categorias = new ArrayList<>();
    private TipoPublicacionDTO tipo;
    private LinkPlataformaDTO Link;
}
