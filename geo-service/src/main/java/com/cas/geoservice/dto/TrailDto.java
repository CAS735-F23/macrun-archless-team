package com.cas.geoservice.dto;

import java.util.List;

public class TrailDto {
    private Long id;
    private String zone;
    private List<CoordinateDto> path;
    private List<PlaceDto> places;
}
