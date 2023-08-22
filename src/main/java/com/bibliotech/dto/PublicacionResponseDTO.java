package com.bibliotech.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PublicacionResponseDTO {
    private Long id;
    private String tituloPublicacion;
    private List<String> nombreAutores = new ArrayList<>();
    private String nombreEditorial;
    private int anioPublicacion;
    private String nombreEdicion;
}