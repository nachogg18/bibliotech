package com.bibliotech.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO {
    @NonNull
    @NotEmpty
    private String descripcion;
    @NonNull
    private Long bibliotecaId;
}
