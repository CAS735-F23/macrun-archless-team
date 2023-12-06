/* (C)2023 */
package com.cas.geoservice.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class MessageDto {
    @NonNull private PlayerSetZoneRequest playerSetZoneRequest;
    @NonNull private String action;
    @Nullable private String message;
}
