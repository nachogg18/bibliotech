package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "plataforma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plataforma extends Base {
    @Column
    private String nombre;
    @Column
    private String urlPlataforma;
    @Column
    private String instrucciones;
    @Column
    private Date fechaAlta = new Date();
    @Column
    private Date fechaBaja;
}
