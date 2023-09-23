package com.bibliotech.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class Usuario extends Base {
    private String email;
    private String pwd;
    private String role;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "prestamo")
//    private List<Prestamo> prestamos;
}
