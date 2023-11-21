package com.cas.geoservice.entity;

import com.cas.geoservice.dto.GeoMapDto;
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
public class GeoMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Place> places;

    public GeoMapDto toDto() {
        return GeoMapDto.builder().places(places).build();
    }
}