package com.bibliotech.dto;

import com.bibliotech.entity.Carrera;
import java.util.Objects;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarreraDTO {
    private Long id;
    private String nombre;
    private String fechaAlta;
    private String fechaBaja;


    public static CarreraDTO toDto(Carrera carrera) {
    return Objects.nonNull(carrera)
        ? CarreraDTO.builder()
            .id(carrera.getId())
            .nombre(carrera.getNombre())
            .fechaAlta(
                Objects.nonNull(carrera.getFechaAlta())
                    ? carrera.getFechaAlta().toString()
                    : "")
            .fechaBaja(
                    Objects.nonNull(carrera.getFechaBaja())
                            ? carrera.getFechaBaja().toString()
                            : "")
            .build()
        : emptyDTO();
    }

    public static CarreraDTO emptyDTO() {
        return CarreraDTO.builder()
                .id(0L)
                .nombre("")
                .fechaAlta("")
                .fechaBaja("")
                .build();
    }

}