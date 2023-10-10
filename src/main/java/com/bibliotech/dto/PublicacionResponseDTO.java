package com.bibliotech.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PublicacionResponseDTO {
    private Long id;
    private String tituloPublicacion;
    private List<String> nombreAutores = new ArrayList<>();
    private String nombreEditorial;
    private int anioPublicacion;
    private String nombreEdicion;
}