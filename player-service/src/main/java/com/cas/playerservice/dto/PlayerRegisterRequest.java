package com.cas.playerservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerRegisterRequest {
    private String username;
    private String password;
    private String email;
    private Integer weight;
    private Integer age;
}
