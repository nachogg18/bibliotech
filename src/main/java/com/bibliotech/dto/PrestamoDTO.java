package com.bibliotech.dto;

import com.bibliotech.entity.Prestamo;
import com.bibliotech.security.dao.response.UserDetailDto;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrestamoDTO {
    private String fechaAlta;
    private String fechaFin;
    private String fechaInicio;
    private List<String> renovaciones;
    private UserDetailDto usuario;
    private EjemplarDetailDTO ejemplar;
    private String estado;
    private MultaDetailDTO multa;


    public static PrestamoDTO toDto(Prestamo prestamo) {
    return Objects.nonNull(prestamo)
        ? PrestamoDTO.builder()
            .fechaAlta(Objects.nonNull(prestamo.getFechaAlta()) ? prestamo.getFechaAlta().toString() : "")
            .fechaFin(Objects.nonNull(prestamo.getFechaFinEstimada()) ? prestamo.getFechaFinEstimada().toString() : "")
            .fechaInicio(Objects.nonNull(prestamo.getFechaInicioEstimada()) ? prestamo.getFechaInicioEstimada().toString() : "")
            .renovaciones(Objects.nonNull(prestamo.getFechasRenovaciones()) ? prestamo.getFechasRenovaciones().stream().map(Instant::toString).toList() : null)
            .usuario(Objects.nonNull(prestamo.getUsuario()) ? UserDetailDto.userToUserDetailDto(prestamo.getUsuario()) : null)
            .ejemplar(Objects.nonNull(prestamo.getEjemplar()) ? EjemplarDetailDTO.toDTO(prestamo.getEjemplar()) : null)
            .estado(Objects.nonNull(prestamo.getEstado()) ? Objects.requireNonNull(prestamo.getEstado().stream().filter(estado -> estado.getFechaFin() == null).findFirst().orElse(null)).getEstado().name() : "")
            .multa(Objects.nonNull(prestamo.getMulta()) ? MultaDetailDTO.toDto(prestamo.getMulta()) : null)
            .build()
        : emptyDTO();
    }

    public static PrestamoDTO emptyDTO() {
        return PrestamoDTO.builder()
                .fechaAlta("")
                .fechaFin("")
                .fechaInicio("")
                .renovaciones(null)
                .usuario(null)
                .ejemplar(null)
                .multa(null)
                .build();
    }

}