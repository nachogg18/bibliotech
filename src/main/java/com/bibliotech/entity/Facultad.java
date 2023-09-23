package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "facultad")
public class Facultad extends Base {
    @Column
    private String nombre;

    @Column
    private Instant fechaAlta = Instant.now();

    @Column
    private Instant fechaBaja;
}
