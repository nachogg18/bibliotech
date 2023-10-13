package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EjemplarDetailDTO {
    private Long id;
    private String nombrePublicacion;
    private String nombreUbicacion;
    private String serialNFC;
}
