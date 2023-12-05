package com.bibliotech.security.entity;

import com.bibliotech.entity.Base;
import com.bibliotech.entity.Carrera;
import com.bibliotech.entity.Facultad;
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
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @ManyToOne
    @JoinColumn(name = "carrera_id")
    private Carrera carrera;

    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;
}
