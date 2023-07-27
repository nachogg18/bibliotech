package com.bibliotech.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "universidad")
public class Edicion extends Base {
   @Column(name="nombreUniversidad")
   private String nombre;
   
   @Column(name="fechaAltaUniversidad")
   private Date fechaAlta = new Date();

   @Column(name="fechaBajaUniversidad")
   private Date fechaBaja = new Date();
}