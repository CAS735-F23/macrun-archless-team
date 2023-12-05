package com.cas.geoservice.entity;

import com.cas.geoservice.dto.PlaceDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Table
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private Double CoordinateX;
    private Double CoordinateY;

    @ManyToOne
    @JoinColumn(name = "trail_id")
    private Trail trail;

    public PlaceDto toDto() {
        return PlaceDto.builder()
                .id(id)
                .name(name)
                .type(type)
                .CoordinateX(CoordinateX)
                .CoordinateY(CoordinateY)
                .trailId(trail.getId())
                .build();
    }
}
