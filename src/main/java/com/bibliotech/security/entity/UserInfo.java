package com.bibliotech.security.entity;

import com.bibliotech.entity.Base;
import com.bibliotech.entity.Localidad;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_info")
@Getter
public class UserInfo extends Base {
    private String legajo;
    private String dni;
    private String telefono;
    private String emailContacto;
    private String direccionContacto;
    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Localidad localidad;
}
