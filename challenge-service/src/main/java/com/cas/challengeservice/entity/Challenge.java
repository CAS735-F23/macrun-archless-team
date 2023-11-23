package com.cas.challengeservice.entity;

import com.cas.challengeservice.constants.ChallengeType;
import com.cas.challengeservice.dto.ChallengeDto;
import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Table
@Entity
public class Challenge {
    @Id
    @Enumerated(EnumType.STRING)
    private ChallengeType challengeType;

    public ChallengeDto toDto() {
        return ChallengeDto.builder()
                .challengeType(challengeType)
                .build();
    }
}
