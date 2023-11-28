package com.cas.geoservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Table
@Entity
public class Trail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zone;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "trail")
    private List<Place> path;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "trail")
    private List<Place> places;
}
