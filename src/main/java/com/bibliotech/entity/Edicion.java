package com.bibliotech.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "edicion")
public class Edicion extends Base {
    @NotNull
    private String nombre;
    @NotNull
    private Date fechaAlta = new Date();
    private Date fechaBaja;
}
