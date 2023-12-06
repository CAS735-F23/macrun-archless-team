/* (C)2023 */
package com.cas.geoservice.entity;

import com.cas.geoservice.dto.PlaceDto;
import com.cas.geoservice.dto.TrailDto;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;

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

    @OneToMany(mappedBy = "trail", cascade = CascadeType.ALL)
    private List<Place> path;

    public TrailDto toDto() {
        List<PlaceDto> placeDtos = path.stream().map(Place::toDto).collect(Collectors.toList());

        return TrailDto.builder().id(id).zone(zone).path(placeDtos).build();
    }
}
