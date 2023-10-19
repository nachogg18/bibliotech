package com.bibliotech.entity;

import com.bibliotech.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name= "prestamo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prestamo extends Base {

    @Column
    private Instant fechaAlta = Instant.now();

    @Column
    private Instant fechaFinEstimada;

    @Column
    private Instant fechaInicioEstimada;

    @Column
    private Instant fechaBaja;

    @ElementCollection(targetClass = Instant.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "fechasRenovacionesPrestamo", joinColumns = @JoinColumn(name = "fechaRenovacion_id"))
    @Column
    private List<Instant> fechasRenovaciones = new ArrayList<>();
    //añadir tag @Transactional al método que vaya a acceder a la lista

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "ejemplar_id")
    private Ejemplar ejemplar;

    @OneToMany
    @JoinColumn(name = "estado_prestamo")
    private List<PrestamoEstado> estado = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "multa_id")
//    private Multa multa;

    public boolean overlapsWith(Instant periodStart, Instant periodEnd) {
        return !this.fechaFinEstimada.isBefore(periodStart) && !periodEnd.isBefore(this.fechaInicioEstimada);
    }
}
