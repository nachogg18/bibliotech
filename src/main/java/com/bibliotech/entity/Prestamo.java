package com.bibliotech.entity;

import com.bibliotech.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import lombok.*;

@Entity
@Builder
@Table(name= "prestamo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prestamo extends Base {

    @Column
    private Instant fechaAlta = Instant.now();

    @Column
    private Instant fechaFinEstimada;

    @Column
    private Instant fechaInicioEstimada;

    @Column
    private Instant fechaBaja;

    @ElementCollection(targetClass = Date.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "fechasRenovacionesPrestamo", joinColumns = @JoinColumn(name = "fechaRenovacion_id"))
    @Column
    private List<Instant> fechasRenovaciones = new ArrayList<>();
    //añadir tag @Transactional al método que vaya acceder a la lista

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ejemplar_id")
    private Ejemplar ejemplar;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_prestamo")
    private List<PrestamoEstado> estado = new ArrayList<PrestamoEstado>();

    @JsonIgnoreProperties("multa_id")
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "multa_id")
    private List<Multa> multa;

    public boolean overlapsWith(Instant periodStart, Instant periodEnd) {
        return !this.fechaFinEstimada.isBefore(periodStart) && !periodEnd.isBefore(this.fechaInicioEstimada);
    }
}
