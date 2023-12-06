/* (C)2023 */
package com.cas.geoservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerSetZoneRequest {

  private String username;
  private String zone;
}
