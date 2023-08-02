package com.bibliotech.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name= "publication")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publication extends Base {

    @Column(name="name")
    private String name;

    @Column(name="start_date")
    private Instant startDate;

    @Column(name="end_date")
    private Instant endDate;

    @Column(name="isbn")
    private String isbn;

    @Column(name="synopsis")
    private String synopsis;

    @Column(name="publication_date")
    private Instant publicationDate;
}