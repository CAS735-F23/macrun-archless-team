/* (C)2023 */
package com.cas.challengeservice.entity;

import com.cas.challengeservice.dto.ChallengeTypeDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Table
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
