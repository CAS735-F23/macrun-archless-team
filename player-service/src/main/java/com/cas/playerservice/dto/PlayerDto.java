package com.cas.playerservice.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class PlayerDto {
    private String username;
    private String email;
    private Integer weight;
    private Integer age;
}
