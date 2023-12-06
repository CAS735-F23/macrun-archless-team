/* (C)2023 */
package com.cas.challengeservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BadgeDeleteRequest {

  private String challenge;
  private String username;
  private String badgeName;
}
