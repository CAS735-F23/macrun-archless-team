/* (C)2023 */
package com.cas.hrmservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class HrmReceiveRequest {
    private String username;
    private Integer heartRate;

    public HeartRateDto toDto() {
        return HeartRateDto.builder().heartRate(heartRate).username(username).build();
    }
}
