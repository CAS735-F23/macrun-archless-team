/* (C)2023 */
package com.cas.geoservice.dto;

import java.util.List;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class TrailDto {

  private Long id;
  private String zone;
  private List<PlaceDto> path;
}
