/* (C)2023 */
package com.cas.geoservice.dto;

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
