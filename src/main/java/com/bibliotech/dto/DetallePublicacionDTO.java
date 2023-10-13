package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;

import com.bibliotech.entity.Edicion;
import com.bibliotech.entity.Editorial;
import com.bibliotech.entity.Link;
import com.bibliotech.entity.Plataforma;
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
    private List<AutorDTO> autores = new ArrayList<>();
    private Edicion edicion;
    private List<Editorial> editoriales = new ArrayList<>();
    private List<DetalleCategoriaDTO> categorias = new ArrayList<>();
    private Link link;
    private Plataforma plataforma;
}
