package com.bibliotech.entity;

import com.bibliotech.security.entity.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

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
    private Instant fechaAlta = Instant.now();
    @Column(name = "fechaBaja")
    private Instant fechaBaja;
    @ManyToOne
    private Ejemplar ejemplar;
    @Column(name = "tipoEvento")
    private TipoEvento tipoEvento;
    @OneToMany
    private List<User> participantes;
}
