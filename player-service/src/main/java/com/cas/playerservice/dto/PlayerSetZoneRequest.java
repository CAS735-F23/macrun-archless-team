/* (C)2023 */
package com.cas.playerservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerSetZoneRequest {
    private String username;
    private String zone;
}
