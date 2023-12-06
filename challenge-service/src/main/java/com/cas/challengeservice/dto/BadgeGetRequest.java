/* (C)2023 */
package com.cas.challengeservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class BadgeGetRequest {
    private String challenge;
    private String username;
}
