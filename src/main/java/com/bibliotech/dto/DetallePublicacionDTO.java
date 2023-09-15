package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DetallePublicacionDTO {
    private String titulo;
    private List<String> autores = new ArrayList<>();
    private String descripcion;
    private String edicion;
    private List<String> editoriales = new ArrayList<>();
    private List<DetalleCategoriaDTO> categorias = new ArrayList<>();
    private List<DetalleEjemplarDTO> ejemplares = new ArrayList<>();
}
