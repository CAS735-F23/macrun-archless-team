/* (C)2023 */
package com.cas.challengeservice.dto;
import lombok.*;
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChallengeGetRequest {
    private Long userId;
    private String reaction;
    private String userHeartRate;
    private String badge;
    private String score;
}
