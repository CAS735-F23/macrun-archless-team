package com.cas.challengeservice.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class ChallengeDto {
    private Long id;
    private String badge;
    private String score;
    private String reaction;
    private String userHeartRate;
}
