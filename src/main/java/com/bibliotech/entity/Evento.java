package com.bibliotech.entity;

import jakarta.persistence.*;
import java.util.Date;
import lombok.*;

@Entity
@Table(name = "evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Evento extends Base {
    @Column(name = "fechaAlta")
    private Date fechaAlta = new Date();
    @Column(name = "fechaBaja")
    private Date fechaBaja;
    @ManyToOne
    private Ejemplar ejemplar;
    @Column(name="tipoEvento")
    private TipoEvento tipoEvento;
}
