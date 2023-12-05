package com.cas.geoservice.dto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class PlaceDto {
    private Long id;
    private String name;
    private String type;
    private Double CoordinateX;
    private Double CoordinateY;
    private Long trailId;
}
