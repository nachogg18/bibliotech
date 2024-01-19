package com.bibliotech.dto;

import com.bibliotech.entity.Ubicacion;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BibliotecaDetalleDTO {
    private String nombre;
    private String ubicacion;
    private String contacto;
    private Instant fechaAlta;
    private Instant fechaBaja;
    private List<UbicacionResponseDTO> ubicaciones;
}
