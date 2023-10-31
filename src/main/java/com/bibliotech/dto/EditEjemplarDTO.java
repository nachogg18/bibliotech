package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditEjemplarDTO {
    private String serialNFC;
    private Long idUbicacion;
    private String estadoEjemplar;
}
