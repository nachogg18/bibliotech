package com.bibliotech.security.entity;

import com.bibliotech.entity.Base;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "resources")
public class Resource extends Base {
    private String name;
}
