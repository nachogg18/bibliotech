package com.bibliotech.dto;

import com.bibliotech.entity.Provincia;
import java.util.Objects;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProvinciaDTO {
    private Long id;
    private String nombre;
    private String codigoPostal;
    private String fechaAlta;
    private String fechaBaja;
    private PaisDTO pais;


    public static ProvinciaDTO toDto(Provincia provincia) {
        return Objects.nonNull(provincia) ?  ProvinciaDTO.builder()
                .id(provincia.getId())
                .nombre(provincia.getNombre())
                .fechaAlta(Objects.nonNull(provincia.getFechaAlta()) ? provincia.getFechaAlta().toString() : "")
                .fechaBaja(Objects.nonNull(provincia.getFechaBaja()) ? provincia.getFechaBaja().toString() : "")
                .pais(Objects.nonNull(provincia.getPais()) ? PaisDTO.toDto(provincia.getPais()) : PaisDTO.emptyDTO())
                .build() : ProvinciaDTO.emptyDTO();
    }

    public static ProvinciaDTO emptyDTO() {
        return ProvinciaDTO.builder()
                .id(0L)
                .nombre("")
                .fechaAlta("")
                .fechaBaja("")
                .pais(PaisDTO.emptyDTO())
                .build();
    }

}