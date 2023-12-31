/* (C)2023 */
package com.cas.playerservice.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class MessageDto {
    @Nullable private PlayerSetZoneRequest playerSetZoneRequest;
    @NonNull private String action;
    @Nullable private String message;
}
