package com.cas.geoservice.entity;

import com.cas.geoservice.dto.PlaceDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Place {
    private String name;
    private Coordinate coordinate;
    private String type;
}
