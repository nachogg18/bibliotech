package com.bibliotech.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name="start_date")
    private LocalDateTime startDate;

    @Column(name="end_date")
    private LocalDateTime endDate;

    @Column(name="isbn")
    private String isbn;

    @Column(name="synopsis")
    private String synopsis;

    @Column(name="publication_date")
    private LocalDateTime publicationDate;
}