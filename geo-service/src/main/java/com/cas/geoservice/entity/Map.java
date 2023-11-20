package com.cas.geoservice.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    private List<Place> places;

    public Map() {
    }

    // getter 和 setter 方法...
}