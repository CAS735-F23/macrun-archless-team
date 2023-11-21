package com.cas.challengeservice.entity;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String badge;
    private String score;
    private String reaction;
    private String userHeartRate;

    public ChallengeDto toDto() {
        return ChallengeDto.builder().badge(badge).score(score).reaction(reaction).userHeartRate(userHeartRate).build();
    }
}
