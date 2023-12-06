/* (C)2023 */
package com.cas.challengeservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChallengeDeleteRequest {

  private String type;
}
