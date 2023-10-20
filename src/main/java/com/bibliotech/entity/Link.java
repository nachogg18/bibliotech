package com.bibliotech.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Builder
@Table(name = "link")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Link extends Base {
    @Column
    private String url;
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;
    @JsonIgnore
    @ManyToOne
    private Plataforma plataforma;
    @Enumerated(EnumType.ORDINAL)
    private EstadoLink estadoLink;
}
