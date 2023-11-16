/* (C)2023 */
package com.cas.playerservice.dto;

import lombok.*;

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
