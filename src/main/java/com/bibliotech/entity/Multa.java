package com.bibliotech.entity;

import com.bibliotech.security.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private TipoMulta tipoMulta;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MultaEstado> multaEstados = new ArrayList<>();
    @ManyToOne
    private Prestamo prestamo;
    @ManyToOne
    @JsonIgnore
    private User user;
}
