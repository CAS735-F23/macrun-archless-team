package com.cas.challengeservice.entity;

import com.cas.challengeservice.dto.ChallengeTypeDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChallengeType {
//    CARDIO("Cardio", 120L, 30L),
//    MUSCLE("Muscle", 80L, 50L),
//    FLEXIBILITY("Flexibility", 70L, 40L),
//    BALANCE("Balance", 70L, 30L);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Long userHeartRate;
    private Long exerciseCount;

    public ChallengeTypeDto toDto() {
        return ChallengeTypeDto.builder()
                .description(description)
                .userHeartRate(userHeartRate)
                .exerciseCount(exerciseCount)
                .build();
    }
}
