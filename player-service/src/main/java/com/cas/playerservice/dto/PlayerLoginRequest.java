package com.cas.playerservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerLoginRequest {
    private String username;
    private String password;
}
