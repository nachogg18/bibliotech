package com.bibliotech.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "notificaciones")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion extends Base{
    private String mensaje;
    private Long userID;
    private boolean leido;
    private TipoNotificacion tipoNotificacion;
}
