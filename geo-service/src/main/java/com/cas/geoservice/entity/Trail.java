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
    @ElementCollection
    private List<Coordinate> path;
    @ElementCollection
    private List<Place> places;
}
