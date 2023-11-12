package com.bibliotech.dto;

import com.bibliotech.entity.Pais;
import java.util.Objects;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaisDTO {
    private Long id;
    private String nombre;
    private String fechaAlta;
    private String fechaBaja;


    public static PaisDTO toDto(Pais pais) {
        return PaisDTO.builder()
                .id(pais.getId())
                .nombre(pais.getNombre())
                .fechaAlta(Objects.nonNull(pais.getFechaAlta()) ? pais.getFechaAlta().toString() : "")
                .fechaBaja(Objects.nonNull(pais.getFechaBaja()) ? pais.getFechaBaja().toString() : "")
                .build();
    }

    public static PaisDTO emptyDTO() {
        return PaisDTO.builder()
                .id(0L)
                .nombre("")
                .fechaAlta("")
                .fechaBaja("")
                .build();
    }

}