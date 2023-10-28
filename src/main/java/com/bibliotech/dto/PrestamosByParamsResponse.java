package com.bibliotech.dto;

import com.bibliotech.entity.Prestamo;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrestamosByParamsResponse {
    private String fechaFinEstimada;
    private String fechaInicioEstimada;
    //private Date fechaBaja;
    private String UsuarioID;
    private String EjemplarID;


    public static PrestamosByParamsResponse prestamoToPrestamoByParamsResponse(Prestamo prestamo) {
        return PrestamosByParamsResponse.builder().
                UsuarioID((Objects.nonNull(prestamo.getUsuario()) && Objects.nonNull(prestamo.getUsuario().getId()) ? prestamo.getUsuario().getId().toString() : ""))
                .EjemplarID((Objects.nonNull(prestamo.getEjemplar()) && Objects.nonNull(prestamo.getEjemplar().getId()) ? prestamo.getEjemplar().toString() : ""))
                .fechaInicioEstimada((Objects.nonNull(prestamo.getFechaInicioEstimada())? prestamo.getFechaInicioEstimada().toString() : ""))
                .fechaFinEstimada((Objects.nonNull(prestamo.getFechaFinEstimada())? prestamo.getFechaFinEstimada().toString() : ""))
                .build();
    }

}
