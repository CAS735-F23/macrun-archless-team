/* (C)2023 */
package com.cas.hrmservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class HeartRateDto {
    private String username;
    private Integer heartRate;
}
