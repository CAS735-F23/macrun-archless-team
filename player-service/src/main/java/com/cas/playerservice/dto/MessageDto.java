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
    @Nullable private PlayerDto playerDto;
    @NonNull private String action;
    private String gameType;
    @Nullable private String message;
}
