package com.cas.geoservice.dto;

import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TrailDto {
    private Long id;
    private String zone;
    private List<PlaceDto> path;
    private List<PlaceDto> places;
}
