package com.bibliotech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditEjemplarDTO {
    private String serialNFC;
    private Long idUbicacion;
    private String estadoEjemplar;
}
