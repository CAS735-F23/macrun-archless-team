package com.cas.challengeservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChallengeDeleteRequest {
    private int userHeartRate;
    private String type;
}
