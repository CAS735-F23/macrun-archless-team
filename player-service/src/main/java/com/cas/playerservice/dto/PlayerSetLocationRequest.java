/* (C)2023 */
package com.cas.playerservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerSetLocationRequest {
    private String username;
    private String location;
}
