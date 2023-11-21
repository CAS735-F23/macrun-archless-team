package com.cas.geoservice.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class PlaceDto {
    private String name;
    private Long x;
    private Long y;
}
