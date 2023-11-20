package com.cas.geoservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int x;
    private int y;

    public Place() {
    }

    // getter 和 setter 方法...
}
