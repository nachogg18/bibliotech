package com.bibliotech.dto;

import com.bibliotech.entity.Multa;
import lombok.*;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MultaDetailDTO {
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String tipo;

    public static MultaDetailDTO toDto(Multa multa) {
        return Objects.nonNull(multa)
                ? MultaDetailDTO.builder()
                .descripcion(Objects.nonNull(multa.getDescripcion()) ? multa.getDescripcion() : "")
                .fechaInicio(Objects.nonNull(multa.getFechaInicio()) ? multa.getFechaInicio().toString() : "")
                .fechaFin(Objects.nonNull(multa.getFechaFin()) ? multa.getFechaFin().toString() : "")
                .tipo(Objects.nonNull(multa.getTipoMulta()) ? multa.getTipoMulta().getNombre() : "")
                .build() : emptyDTO();
    }

    public static MultaDetailDTO emptyDTO() {
        return MultaDetailDTO.builder()
                .descripcion("")
                .tipo("")
                .fechaFin("")
                .fechaInicio("")
                .build();
    }
}
