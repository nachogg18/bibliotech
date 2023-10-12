package com.bibliotech.dto;

import com.bibliotech.entity.Plataforma;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkPlataformaDTO {
    private Long id;
    private String estado;
    private String url;
    private Plataforma plataforma;
}
