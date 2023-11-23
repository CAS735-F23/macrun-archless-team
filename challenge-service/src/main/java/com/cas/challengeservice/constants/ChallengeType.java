package com.cas.challengeservice.constants;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ChallengeType {
    CARDIO("Cardio", 120L, 30L),
    MUSCLE("Muscle", 80L, 50L),
    FLEXIBILITY("Flexibility", 70L, 40L),
    BALANCE("Balance", 70L, 30L);

    private String description;
    private Long averageHeartRate;
    private Long exerciseCount;
}
