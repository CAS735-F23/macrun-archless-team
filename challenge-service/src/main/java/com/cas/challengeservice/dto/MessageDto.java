package com.cas.challengeservice.dto;
import jakarta.annotation.Nullable;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class MessageDto {
    @Nullable private ChallengeDto challengeDto;
    @NonNull private String userHeartRate;
    @NonNull private String reaction;
    @Nullable private String message;
}
