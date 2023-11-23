package com.cas.geoservice.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto {
    private String name;
    private CoordinateDto coordinate;
    private String type;
}
