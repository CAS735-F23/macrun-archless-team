/* (C)2023 */
package com.cas.challengeservice.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class MessageDto {
    @NonNull private BadgeAddRequest badgeAddRequest;
    @NonNull private String action;
    @Nullable private String message;
}
