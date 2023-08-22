package com.bibliotech.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DetallePublicacionDTO {
    private String tituloPublicacion;
    private List<String> nombreAutores = new ArrayList<>();
    private String nombreEditorial;
    private String nombreEdicion;
    private String descripcion;
//    private List<Categoria> categoriaList = new ArrayList<>();
    private List<EjemplarDTO> ejemplarDTOList = new ArrayList<>();
}
