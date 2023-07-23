package com.bibliotech.dto;

import lombok.Data;

import java.util.List;

@Data
public class PublicacionDTO {
    private String tituloPublicacion;
    private List<String> nombreAutores;
    private String nombreEditorial;
    private int anioPublicacion;
}
