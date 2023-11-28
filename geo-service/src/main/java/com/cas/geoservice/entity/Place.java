package com.cas.geoservice.entity;

import com.cas.geoservice.dto.PlaceDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Coordinate coordinate;

    private String type;

    @ManyToOne
    private Trail trail;
}
