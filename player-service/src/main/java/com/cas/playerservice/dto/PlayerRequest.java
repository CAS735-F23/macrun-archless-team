/* (C)2023 */
package com.cas.playerservice.dto;

import jakarta.annotation.Nullable;
import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerRequest {
    @NonNull private String username;
    @Nullable private String password;
}
