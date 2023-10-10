package com.bibliotech.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@Table(name = "editorial")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Editorial extends Base {
    @Column
    private String nombre;
    @Column
    private Instant fechaAlta = Instant.now();
    @Column
    private Instant fechaBaja;

}
