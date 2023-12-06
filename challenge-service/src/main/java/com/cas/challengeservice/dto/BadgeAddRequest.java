/* (C)2023 */
package com.cas.challengeservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class BadgeAddRequest {
    private String challenge;
    private String username;
    private String badgeName;
}
