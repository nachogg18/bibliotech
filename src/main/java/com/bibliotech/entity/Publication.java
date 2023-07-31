package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name= "publication")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publication extends Base {

    @Column(name="name")
    private String name;
}