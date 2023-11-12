package com.bibliotech.dto;

import com.bibliotech.entity.Ejemplar;
import lombok.*;

import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EjemplarDetailDTO {
    private Long id;
    private String nombrePublicacion;
    private String nombreUbicacion;
    private String serialNFC;

    public static EjemplarDetailDTO toDTO (Ejemplar ejemplar) {
        return Objects.nonNull(ejemplar)
                ? EjemplarDetailDTO.builder()
                .id(Objects.nonNull(ejemplar.getId()) ? ejemplar.getId() : null)
                .nombrePublicacion(Objects.nonNull(ejemplar.getPublicacion()) ? ejemplar.getPublicacion().getTitulo() : "")
                .nombreUbicacion(Objects.nonNull(ejemplar.getUbicacion()) ? ejemplar.getUbicacion().getDescripcion() : "")
                .serialNFC(Objects.nonNull(ejemplar.getSerialNFC()) ? ejemplar.getSerialNFC() : "")
                .build() : emptyDTO();
    }

    public static EjemplarDetailDTO emptyDTO() {
        return EjemplarDetailDTO.builder()
                .id(null)
                .nombreUbicacion("")
                .nombrePublicacion("")
                .serialNFC("")
                .build();
    }
}
