/* (C)2023 */
package com.cas.challengeservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ChallengeTypeDto {

  private Long id;
  private String description;
  private Long userHeartRate;
  private Long exerciseCount;
}
