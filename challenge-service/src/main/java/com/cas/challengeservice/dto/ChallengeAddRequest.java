package com.cas.challengeservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChallengeAddRequest {
    private Long userHeartRate;
    private String type;
}