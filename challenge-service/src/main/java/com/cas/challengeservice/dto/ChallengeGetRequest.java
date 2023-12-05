package com.cas.challengeservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ChallengeGetRequest {
    private Long userHeartRate;
    private String type;
}
