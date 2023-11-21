package com.cas.geoservice.entity;

import com.cas.geoservice.dto.PlaceDto;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Long x;
    private Long y;

    public PlaceDto toDto() {
        return PlaceDto.builder().name(name).x(x).y(y).build();
    }
}
