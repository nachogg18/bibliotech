package com.bibliotech.dto;

import com.bibliotech.entity.Localidad;
import java.util.Objects;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocalidadDTO {
    private Long id;
    private String nombre;
    private String codigoPostal;
    private String fechaAlta;
    private String fechaBaja;
    private ProvinciaDTO provincia;


    public static LocalidadDTO toDto(Localidad localidad) {
    return Objects.nonNull(localidad)
        ? LocalidadDTO.builder()
            .id(localidad.getId())
            .nombre(localidad.getNombre())
            .codigoPostal(
                Objects.nonNull(localidad.getCodigoPostal()) ? localidad.getCodigoPostal() : "")
            .fechaAlta(
                Objects.nonNull(localidad.getFechaAlta())
                    ? localidad.getFechaAlta().toString()
                    : "")
            .provincia(
                Objects.nonNull(ProvinciaDTO.toDto(localidad.getProvincia()))
                    ? ProvinciaDTO.toDto(localidad.getProvincia())
                    : ProvinciaDTO.emptyDTO())
            .build()
        : emptyDTO();
    }

    public static LocalidadDTO emptyDTO() {
        return LocalidadDTO.builder()
                .id(0L)
                .nombre("")
                .codigoPostal("")
                .fechaAlta("")
                .provincia(ProvinciaDTO.emptyDTO())
                .build();
    }

}