package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;

import com.bibliotech.entity.Autor;
import com.bibliotech.entity.Editorial;
import com.bibliotech.entity.Link;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetallePublicacionDTO {
    private Long id;
    private String tituloPublicacion;
    private String isbnPublicacion;
    private Integer nroPaginas;
    private Integer anioPblicacion;
    private List<Autor> autores = new ArrayList<>();
    private String edicion;
    private List<Editorial> editoriales = new ArrayList<>();
    private List<DetalleCategoriaDTO> categorias = new ArrayList<>();
    //private List<DetalleEjemplarDTO> ejemplares = new ArrayList<>();
    private Link link;
}
