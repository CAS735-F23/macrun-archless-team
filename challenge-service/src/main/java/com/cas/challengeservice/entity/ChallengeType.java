package com.cas.challengeservice.entity;

import com.cas.challengeservice.dto.ChallengeTypeDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChallengeType {
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
