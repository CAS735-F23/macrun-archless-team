package com.cas.geoservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SetLocationRequest {
    private Long x;
    private Long y;
}
