package com.cas.challengeservice.dto;
import com.cas.challengeservice.constants.ChallengeType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
public class ChallengeDto {
    private ChallengeType challengeType;
}
