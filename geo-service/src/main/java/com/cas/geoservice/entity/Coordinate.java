package com.cas.geoservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinate {
    private Double x;
    private Double y;
}
