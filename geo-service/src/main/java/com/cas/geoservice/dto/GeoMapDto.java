package com.cas.geoservice.dto;
import com.cas.geoservice.entity.Place;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class GeoMapDto {
    private List<Place> places;
}
