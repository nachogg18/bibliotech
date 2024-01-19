package com.bibliotech.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionDTO {
    @NonNull
    @NotEmpty
    private String descripcion;
    @NonNull
    private Long bibliotecaId;
}
