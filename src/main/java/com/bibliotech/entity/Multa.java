package com.bibliotech.entity;

import com.bibliotech.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Entity
@Table(name = "multa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Multa extends Base {

    @Column
    private String descripcion;

    @Column
    private Instant fechaInicio;

    @Column
    private Instant fechaFin;

    @Column
    private Instant fechaAlta = Instant.now();

    @Column
    private Instant fechaBaja;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoMulta tipoMulta;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MultaEstado> multaEstados = new ArrayList<>();

    @JsonIgnoreProperties("prestamo_id")
    @ManyToOne
    @JoinColumn(name = "prestamo_id")
    private Prestamo prestamo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
