/* (C)2023 */
package com.cas.hrmservice.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class MessageDto {
    @Nullable private HeartRateDto heartRateDto;
    @NonNull private String action;
    @Nullable private String message;
}
