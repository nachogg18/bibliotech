package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Date;
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
@Table(name = "universidad")
public class Universidad extends Base {
   @Column(name="nombreUniversidad")
   private String nombre;
   
   @Column(name="fechaAltaUniversidad")
   private Date fechaAlta = new Date();

   @Column(name="fechaBajaUniversidad")
   private Date fechaBaja = new Date();
}