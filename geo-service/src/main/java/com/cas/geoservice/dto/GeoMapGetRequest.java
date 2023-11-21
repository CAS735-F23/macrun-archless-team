package com.cas.geoservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class GeoMapGetRequest {
    private Long geoMapId;
}
