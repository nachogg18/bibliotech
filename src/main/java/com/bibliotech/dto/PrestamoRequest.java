package com.bibliotech.dto;

import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrestamoRequest {
    @NotBlank (message = "fecha fin estimada obligatorio")
    @Future (message = "fecha fin estimada debe ser futura")
    private Instant fechaFinEstimada;
    @NotBlank (message = "fecha inicio estimada obligatorio")
    private Instant fechaInicioEstimada;
    @NotBlank (message = "usuarioID obligatorio")
    private Long UsuarioID;
    @NotBlank (message = "ejemplarID obligatorio")
    private Long EjemplarID;
    //private Date fechaBaja;
}
