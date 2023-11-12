package com.bibliotech.dto;

import com.bibliotech.entity.Facultad;
import java.util.Objects;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacultadDTO {
    private Long id;
    private String nombre;
    private String fechaAlta;
    private String fechaBaja;


    public static FacultadDTO toDto(Facultad facultad) {
    return Objects.nonNull(facultad)
        ? FacultadDTO.builder()
            .id(facultad.getId())
            .nombre(facultad.getNombre())
            .fechaAlta(
                Objects.nonNull(facultad.getFechaAlta())
                    ? facultad.getFechaAlta().toString()
                    : "")
            .fechaBaja(
                    Objects.nonNull(facultad.getFechaBaja())
                            ? facultad.getFechaBaja().toString()
                            : "")
            .build()
        : emptyDTO();
    }

    public static FacultadDTO emptyDTO() {
        return FacultadDTO.builder()
                .id(0L)
                .nombre("")
                .fechaAlta("")
                .fechaBaja("")
                .build();
    }

}